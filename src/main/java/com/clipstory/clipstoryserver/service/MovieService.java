package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.responseDto.PagedResponseDto;

import java.util.*;

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

    private final HashMap<Long, HashMap<String, HashMap<String, Double>>> movieVectors = new HashMap<>();

    private final HashMap<Long, HashMap<String, Double>> movieVectorsMagnitude = new HashMap<>();

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

    public Double magnitude(Movie movie, String base) {
        if (movieVectorsMagnitude.get(movie.getId()) == null) {
            movieVectorsMagnitude.put(movie.getId(), new HashMap<>());
        }
        if (movieVectorsMagnitude.get(movie.getId()).get(base) != null) {
            return movieVectorsMagnitude.get(movie.getId()).get(base);
        }

        Double magnitude = 0.0;
        HashMap<String, Double> movieVector = getMovieVector(movie, base);
        for (Double count : movieVector.values()) {
            magnitude += count * count;
        }

        magnitude = Math.sqrt(magnitude);

        movieVectorsMagnitude.get(movie.getId()).put(base, magnitude);
        return magnitude;
    }

    public Double dotProduct(Movie movie1, Movie movie2, String base) {
        HashMap<String, Double> movie1Vector = getMovieVector(movie1, base);
        HashMap<String, Double> movie2Vector = getMovieVector(movie2, base);

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

    public HashMap<String, Double> getMovieVector(Movie movie, String base) {
        if (movieVectors.get(movie.getId()) == null) {
            movieVectors.put(movie.getId(), new HashMap<>());
        }
        if (movieVectors.get(movie.getId()).get(base) != null) {
            return movieVectors.get(movie.getId()).get(base);
        }

        HashMap<String, Double> movieVector = new HashMap<>();
        if (base.equals("GENRE")) {
            for (Genre genre : movie.getGenres()) {
                movieVector.merge(genre.getName(), 1.0, Double::sum);
            }
        } else if (base.equals("TAG")) {
            for (Tag tag : tagService.getTagsByMovieId(movie.getId())) {
                movieVector.merge(tag.getContent(), 1.0, Double::sum);
            }
        }

        movieVectors.get(movie.getId()).put(base, movieVector);
        return movieVector;
    }
}
