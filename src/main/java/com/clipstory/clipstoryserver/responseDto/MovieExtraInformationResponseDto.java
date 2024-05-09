package com.clipstory.clipstoryserver.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MovieExtraInformationResponseDto {

    private String poster_path;

    private boolean adult;

    private String overview;

}
