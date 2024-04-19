package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.responseDto.PagedResponseDto;
import com.clipstory.clipstoryserver.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@Tag(name = "movie", description = "영화관련 api")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/")
    @Operation(summary = "영화 전체 조회")
    public ApiResponse<?> getAllMovies(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MovieResponseDto> pagedMovieList = movieService.getMovies(pageable);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), pagedMovieList);
    }

    @GetMapping("/{movieId}")
    @Operation(summary = "영화 조회")
    public ApiResponse<?> getMovie(
            @PathVariable Long movieId
    ) throws IOException {
        MovieResponseDto movieList = movieService.getMovie(movieId);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), movieList);
    }

}
