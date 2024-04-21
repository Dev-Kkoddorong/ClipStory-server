package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.responseDto.MovieSuggestionService;
import com.clipstory.clipstoryserver.responseDto.PagedResponseDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;

    private final RatingService ratingService;

    private final TagService tagService;

    public void createMovie(Long movieId, Long tId, String title, Set<Genre> genres) {
        Movie movie = Movie.toEntity(movieId, tId, title, genres);
        movieRepository.save(movie);
    }

    public PagedResponseDto<MovieResponseDto> getMovies(Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return new PagedResponseDto<>(movies.
                map(movie ->  MovieResponseDto.toMovieResponseDto(
                        movie, ratingService.getAverageRating(movie.getId()),
                        tagService.getTagsByMovieId(movie.getId()))));
    }

    public MovieResponseDto getMovie(Long movieId) {
        Movie movie = findMovieById(movieId);
        return MovieResponseDto.toMovieResponseDto(movie,
                ratingService.getAverageRating(movieId), tagService.getTagsByMovieId(movie.getId()));
    }

    public PagedResponseDto<MovieResponseDto> getMovieByPartOfTitle(String partOfTitle, Pageable pageable) {
        Page<Movie> movies = movieRepository.findByTitleContainingKeyword(partOfTitle, pageable);
        return new PagedResponseDto<>(movies.
                map(movie ->  MovieResponseDto.toMovieResponseDto(
                        movie, ratingService.getAverageRating(movie.getId()),
                        tagService.getTagsByMovieId(movie.getId()))));
    }


    public Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.MOVIE_NOT_FOUND));
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    public Double magnitude(Movie movie) {
        Double magnitude = 0.0;
        HashMap<String, Double> movieVector = getMovieVector(movie);
        for (Double count : movieVector.values()) {
            magnitude += count * count;
        }

        magnitude = Math.sqrt(magnitude);
        return magnitude;
    }

    public Double dotProduct(Movie movie1, Movie movie2) {
        HashMap<String, Double> movie1Vector = getMovieVector(movie1);
        HashMap<String, Double> movie2Vector = getMovieVector(movie2);

        Set<String> keySet = new HashSet<>();
        keySet.addAll(movie1Vector.keySet());
        keySet.addAll(movie2Vector.keySet());

        Double dotProduct = 0.0;
        for (String key : keySet) {
            if (movie1Vector.get(key) == null || movie2Vector.get(key) == null) {
                continue;
            }

            dotProduct += movie1Vector.get(key) * movie2Vector.get(key);
        }

        return dotProduct;
    }

    public HashMap<String, Double> getMovieVector(Movie movie) {
        HashMap<String, Double> movieVector = new HashMap<>();
        for (Genre genre : movie.getGenres()) {
            movieVector.merge(genre.getName(), MovieSuggestionService.GENRE_WEIGHT, Double::sum);
        }
        for (Tag tag : tagService.getTagsByMovieId(movie.getId())) {
            movieVector.merge(tag.getContent(), 1.0, Double::sum);
        }
        return movieVector;
    }
}
