package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class MovieResponseDto {

    private Long id;

    private String title;

    private Long tId;

    private Set<String> genreNameList;

    private Double averageRating;

    public static MovieResponseDto toMovieResponseDto(Movie movie, Double averageRating) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .tId(movie.getTId())
                .genreNameList(movie.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.toSet()))
                .averageRating(averageRating)
                .build();
    }

}
