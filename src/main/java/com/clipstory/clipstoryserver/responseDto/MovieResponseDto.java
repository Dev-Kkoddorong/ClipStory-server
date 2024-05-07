package com.clipstory.clipstoryserver.responseDto;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
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

    private List<String> tagList;

    private Double averageRating;

    private List<Long> ratingIdList;

    private String imageUrl;

    private Boolean isAdult;

    private String overView;

    public static MovieResponseDto toMovieResponseDto(Movie movie, Double averageRating, List<Tag> tagList, MovieExtraInformationResponseDto movieExtraInformationResponseDto) {

        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .tId(movie.getTId())
                .genreNameList(movie.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.toSet()))
                .tagList(tagList.stream().map(tag -> tag.getContent()).collect(Collectors.toList()))
                .ratingIdList(movie.getRatings().stream().map(rating -> rating.getId()).collect(Collectors.toList()))
                .averageRating(averageRating)
                .imageUrl(movieExtraInformationResponseDto.getPoster_path())
                .isAdult(movieExtraInformationResponseDto.isAdult())
                .overView(movieExtraInformationResponseDto.getOverview())
                .build();

    }

}
