package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.requestDto.MovieSuggestionRequestDto;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.service.MovieService;
import com.clipstory.clipstoryserver.service.RatingService;
import com.clipstory.clipstoryserver.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MovieSuggestionService {

    private final MovieService movieService;

    private final RatingService ratingService;

    private final TagService tagService;


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

        return similarMovies.stream()
                .map(movie -> MovieResponseDto.toMovieResponseDto(
                        movie, ratingService.getAverageRating(movie.getId()), tagService.getTagsByMovieId(movie.getId())
                    )
                )
                .sorted(Comparator.comparingDouble(
                            (MovieResponseDto movieResponseDto) -> movieResponseDto.getAverageRating() == null ? 0 : movieResponseDto.getAverageRating()
                        ).reversed()
                )
                .toList().subList(0, SUGGESTION_MOVIE_SIZE);
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
            } if (ratingService.countRatingByMovieId(movie.getId()) < COUNT_RATING_TO_PASS) {
                continue;
            } if (!isSimilarMovie(myMovie, movie)) {
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

        Double result = (m1 == 0.0 || m2 == 0.0 ? 0.0 : (d / (m1 * m2)));
        return result;
    }

}
