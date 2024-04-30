package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.responseDto.GenreResponseDto;
import com.clipstory.clipstoryserver.responseDto.MovieResponseDto;
import com.clipstory.clipstoryserver.responseDto.PagedResponseDto;
import com.clipstory.clipstoryserver.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
@Tag(name = "genre", description = "장르 관련 api")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/")
    @Operation(summary = "장르 전체 조회")
    public ApiResponse<?> getAllGenres() throws IOException {
        List<GenreResponseDto> genreResponseDtoList = genreService.getAllGenres();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), genreResponseDtoList);
    }

}
