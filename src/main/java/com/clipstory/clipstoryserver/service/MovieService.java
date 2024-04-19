package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MovieRepository;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    private final RatingService ratingService;

    public void createMovie(Long movieId, Long tId, String title, Set<Genre> genres) {
        Movie movie = Movie.toEntity(movieId, tId, title, genres);
        movieRepository.save(movie);
    }

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream()
                .map(movie ->  MovieResponseDto.toMovieResponseDto(movie, ratingService.getAverageRating(movie.getId())))
                .collect(Collectors.toList());
    }

    public MovieResponseDto getMovie(Long movieId) {
        Movie movie = findMovieById(movieId);
        return MovieResponseDto.toMovieResponseDto(movie, ratingService.getAverageRating(movieId));
    }


    public Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.MOVIE_NOT_FOUND));
    }

}