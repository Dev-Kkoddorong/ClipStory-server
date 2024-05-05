package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.requestDto.LoginRequestDto;
import com.clipstory.clipstoryserver.responseDto.GenreResponseDto;
import com.clipstory.clipstoryserver.responseDto.JwtResponseDto;
import com.clipstory.clipstoryserver.service.AuthService;
import com.clipstory.clipstoryserver.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "인증 관련 api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<?> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto) {
        JwtResponseDto jwtResponseDTO = authService.login(loginRequestDto);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), jwtResponseDTO);
    }

}
