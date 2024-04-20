package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponseDto {

    Long id;

    String memberName;

    String movieTitle;

    String content;

    LocalDateTime createdAt;

    public static TagResponseDto toResponseDto(Tag tag) {
        return TagResponseDto.builder()
                .id(tag.getId())
                .memberName(tag.getMember().getName())
                .movieTitle(tag.getMovie().getTitle())
                .content(tag.getContent())
                .createdAt(tag.getCreatedAt())
                .build();
    }
}
