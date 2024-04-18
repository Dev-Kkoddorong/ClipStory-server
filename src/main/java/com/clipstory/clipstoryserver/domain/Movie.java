package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.service.GenreService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private Long tId;

    @ManyToMany
    private Set<Genre> genres;

    public static Movie toEntity(Long id, Long tId, StringBuilder title, String[] genre, GenreService genreService) {
        return Movie.builder()
                .id(id)
                .tId(tId)
                .title(title.toString())
                .genres(Arrays.stream(genre)
                        .map(genreService::findOrCreateNew)
                        .collect(Collectors.toSet()))
                .build();
    }

}
