package com.clipstory.clipstoryserver.responseDto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JwtResponseDto {

    private String accessToken;

    private String refreshToken;

}