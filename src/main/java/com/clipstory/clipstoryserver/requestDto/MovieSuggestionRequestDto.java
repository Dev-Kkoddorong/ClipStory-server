package com.clipstory.clipstoryserver.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieSuggestionRequestDto {

    @Schema(description = "좋아하는 영화 id 리스트", example = "[ 1,2,3 ]")
    private List<Long> likeMovieIdList;

    @Schema(description = "싫어하는 영화 id 리스트", example = "[ 1,2 ]")
    private List<Long> hateMovieIdList;

}