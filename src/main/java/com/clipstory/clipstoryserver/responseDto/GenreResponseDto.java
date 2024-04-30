package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Genre;
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
public class GenreResponseDto {

    private Long id;

    private String name;

    public static GenreResponseDto toGenreResponseDto(Genre genre) {
        return GenreResponseDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

}
