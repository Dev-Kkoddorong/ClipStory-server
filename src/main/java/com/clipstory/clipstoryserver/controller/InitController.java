package com.clipstory.clipstoryserver.controller;

import com.clipstory.clipstoryserver.global.response.ApiResponse;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.service.InitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/init")
@Tag(name = "init", description = "초기세팅 api")
public class InitController {

    private final InitService initService;

    @GetMapping("/")
    @Operation(summary = "DB에 데이터 적재")
    public ApiResponse<?> init() throws IOException {
        initService.addData();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), null);
    }

}
