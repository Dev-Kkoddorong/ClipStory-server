package com.clipstory.clipstoryserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Token {

    private String accessToken;

    private String refreshToken;

}