package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "test", description = "테스트 api")
public class TestController {

    @PostMapping("/")
    @Operation(summary = "테스트")
    public ApiResponse<?> login() {
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), null);
    }

}
