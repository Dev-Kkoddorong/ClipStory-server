package com.clipstory.clipstoryserver.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Token {

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

}