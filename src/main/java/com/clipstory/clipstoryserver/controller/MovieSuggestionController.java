package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.requestDto.MovieSuggestionRequestDto;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.responseDto.MovieSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movieSuggestion")
@Tag(name = "movieSuggestion", description = "영화 추천 api")
public class MovieSuggestionController {

    private final MovieSuggestionService movieSuggestionService;

    @Operation(summary = "유사한 영화 추천")
    @PostMapping("/similarMovie")
    public ApiResponse<List<MovieResponseDto>> getSimilarMovies(
            @RequestBody MovieSuggestionRequestDto movieSuggestionRequestDto
    ) {
        List<MovieResponseDto> movieResponseDtos = movieSuggestionService.getLikableMovies(movieSuggestionRequestDto);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), movieResponseDtos);
    }

}
