package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Slf4j
public class MovieResponseDto {

    private Long id;

    private String title;

    private Long tId;

    private Set<String> genreNameList;

    private List<String> tagList;

    private Double averageRating;

    private String imageUrl;

    private Boolean isAdult;

    private String overView;

    public static MovieResponseDto toMovieResponseDto(Movie movie, MovieExtraInformationResponseDto movieExtraInformationResponseDto) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .tId(movie.getTId())
                .genreNameList(movie.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
                .tagList( movie.getTags().stream().map(Tag::getContent).collect(Collectors.toList()))
                .averageRating(movie.getAverageRating())
                .imageUrl(movieExtraInformationResponseDto != null ? movieExtraInformationResponseDto.getPoster_path() : null)
                .isAdult(movieExtraInformationResponseDto != null ? movieExtraInformationResponseDto.isAdult() : null)
                .overView(movieExtraInformationResponseDto != null ? movieExtraInformationResponseDto.getOverview() : null)
                .build();
    }

}
