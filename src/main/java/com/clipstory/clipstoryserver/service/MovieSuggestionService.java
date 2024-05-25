package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.requestDto.MovieSuggestionRequestDto;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.service.MovieService;
import com.clipstory.clipstoryserver.service.RatingService;
import com.clipstory.clipstoryserver.service.TagService;
import jakarta.websocket.OnError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class MovieSuggestionService {

    private final MovieService movieService;

    private final RatingService ratingService;

    private final TagService tagService;

    private final MemberService memberService;

    private final GenreService genreService;


    private static final int LIKE_MOVIE_SIZE = 3;

    private static final int HATE_MOVIE_SIZE = 3;

    private static final int SUGGESTION_MOVIE_SIZE = 3;

    private static final Double GENRE_SIMILARITY_TO_PASS = 0.9;

    private static final Double TAG_SIMILARITY_TO_PASS = 0.0;

    private static final Long COUNT_RATING_TO_PASS = 5L;

    public List<MovieResponseDto> getLikableMovies(MovieSuggestionRequestDto movieSuggestionRequestDto) {
        List<Movie> likeMovies = movieSuggestionRequestDto.getLikeMovieIdList()
                .stream()
                .map(movieService::findMovieById)
                .toList();
        List<Movie> hateMovies = movieSuggestionRequestDto.getHateMovieIdList()
                .stream()
                .map(movieService::findMovieById)
                .toList();

        Set<Movie> similarMovies = getSimilarMovies(likeMovies);
        similarMovies.removeAll(getSimilarMovies(hateMovies));

        return similarMovies.stream().sorted(Comparator.comparingDouble(
                Movie::getAverageRating).reversed()).toList()
                .subList(0, Math.min(SUGGESTION_MOVIE_SIZE, similarMovies.size()))
                .stream()
                .map(movie ->
                        MovieResponseDto.toMovieResponseDto(
                                movie, movie.getAverageRating(),
                                movie.getTags(),
                                movieService.addMovieInformation(movie))
                        )
                .toList();
    }

    public Set<Movie> getSimilarMovies(List<Movie> movies) {
        Set<Movie> similarMovies = new HashSet<>();
        for (Movie movie : movies) {
            similarMovies.addAll(getSimilarMovies(movie));
        }
        return similarMovies;
    }

    public Set<Movie> getSimilarMovies(Movie myMovie) {
        Set<Movie> similarMovies = new HashSet<>();

        for (Movie movie : movieService.findAllMovies()) {
            if (Objects.equals(movie.getId(), myMovie.getId())) {
                continue;
            }
            if (movie.getRatings().size() < COUNT_RATING_TO_PASS) {
                continue;
            }
            if (!isSimilarMovie(myMovie, movie)) {
                continue;
            }

            similarMovies.add(movie);
        }
        return similarMovies;
    }

    boolean isSimilarMovie(Movie movie1, Movie movie2) {
        Double genreSimilarity = CosineSimilarityByBase(movie1, movie2,"GENRE");
        if (genreSimilarity >= GENRE_SIMILARITY_TO_PASS) {
            return true;
        }

        Double tagSimilarity = CosineSimilarityByBase(movie1, movie2,"TAG");
        if (tagSimilarity > TAG_SIMILARITY_TO_PASS) {
            return true;
        }

        return false;
    }

    private Double CosineSimilarityByBase(Movie movie1, Movie movie2, String base) {
        Double d = movieService.dotProduct(movie1, movie2, base);
        Double m1 = movieService.magnitude(movie1, base);
        Double m2 = movieService.magnitude(movie2, base);

        Double similarity = (m1 == 0.0 || m2 == 0.0 ? 0.0 : (d / (m1 * m2)));
        return similarity;
    }

    public List<MovieResponseDto> getSimilarPeoplesMovies(MovieSuggestionRequestDto movieSuggestionRequestDto) {
        List<Movie> likeMovies = movieSuggestionRequestDto.getLikeMovieIdList()
                .stream()
                .map(movieService::findMovieById)
                .toList();
        List<Movie> hateMovies = movieSuggestionRequestDto.getHateMovieIdList()
                .stream()
                .map(movieService::findMovieById)
                .toList();

        List<Double> myPos = memberService.getMemberPos(likeMovies, hateMovies);
        List<MemberDist> memberDists = getMemberDists(myPos);

        Collections.sort(memberDists);

        Set<Movie> similarPeoplesMovies = new HashSet<>();
        int idx = 0;
        while (similarPeoplesMovies.size() < SUGGESTION_MOVIE_SIZE) {
            Member member = memberService.findById(memberDists.get(idx).memberId);
            Movie movie = member.getBestMovie();
            similarPeoplesMovies.add(movie);
            idx++;
        }
        return similarPeoplesMovies.stream()
                .map(movie ->
                        MovieResponseDto.toMovieResponseDto(
                                movie, movie.getAverageRating(),
                                movie.getTags(),
                                movieService.addMovieInformation(movie))
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberDist> getMemberDists(List<Double> pos) {
        List<Member> members = memberService.findAllMember();

        members.forEach(member -> {
            Hibernate.initialize(member.getRatingList());
            member.getRatingList().forEach(rating -> {
                Hibernate.initialize(rating.getMovie());
                Hibernate.initialize(rating.getMovie().getGenres());
            });
        });

        return members.parallelStream()
                .map(member -> {
                    List<Double> memberPos = memberService.getMemberPos(member.getId());
                    return new MemberDist(getMemberDist(pos, memberPos), member.getId());
                })
                .collect(Collectors.toList());
    }


    public Double getMemberDist(List<Double> pos1, List<Double> pos2) {
        int genreSize = (int)genreService.genreSize();

        // 병렬 스트림을 사용하여 거리 계산
        double dist = IntStream.range(0, genreSize)
                .parallel()
                .mapToDouble(i -> {
                    double delta = pos1.get(i) - pos2.get(i);
                    return delta * delta;
                })
                .sum();

        return Math.sqrt(dist);
    }

    class MemberDist implements Comparable<MemberDist> {
        Double dist;

        Long memberId;

        MemberDist(Double dist, Long memberId) {
            this.dist = dist;
            this.memberId = memberId;
        }

        @Override
        public int compareTo(MemberDist other) {
            int distComp = this.dist.compareTo(other.dist);
            if (distComp != 0) {
                return distComp;
            }
            return this.memberId.compareTo(other.memberId);
        }

    }

}
