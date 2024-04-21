package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.requestDto.MovieSuggestionRequestDto;
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

    private static final Double SIMILARITY_TO_PASS = 0.90;

    public static final Double GENRE_WEIGHT = 3.0;

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
        //similarMovies.removeAll(getSimilarMovies(hateMovies));
        return similarMovies
                .stream()
                .map(movie -> MovieResponseDto.toMovieResponseDto(
                        movie, ratingService.getAverageRating(movie.getId()), tagService.getTagsByMovieId(movie.getId())
                        )
                )
                .sorted(Comparator.comparingDouble(
                        (MovieResponseDto movieResponseDto) -> movieResponseDto.getAverageRating() == null ? 0 : movieResponseDto.getAverageRating()
                        )
                        .reversed()
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

            Double similarity = calculateSimilarity(myMovie, movie);
            //log.info(similarity.toString());

            if (similarity >= SIMILARITY_TO_PASS) {
                /*log.info(myMovie.getTitle());
                log.info(movie.getTitle());
                log.info(similarity.toString());
                log.info("");*/
                similarMovies.add(movie);
            }
        }
        return similarMovies;
    }

    private Double calculateSimilarity(Movie movie1, Movie movie2) {
        return CosineSimilarity(movie1, movie2);
    }

    private Double CosineSimilarity(Movie movie1, Movie movie2) {
        Double d = movieService.dotProduct(movie1, movie2);
        Double m1 = movieService.magnitude(movie1);
        Double m2 = movieService.magnitude(movie2);

        return d / (m1 * m2);
    }


}
