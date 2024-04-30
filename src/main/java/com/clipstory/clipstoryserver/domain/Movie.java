package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.service.GenreService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;

import java.sql.Wrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private Long id;

    private String title;

    private Long tId;

    @ManyToMany
    private Set<Genre> genres;

    public static Movie toEntity(Long id, Long tId, String title, Set<Genre> genres) {
        return Movie.builder()
                .id(id)
                .tId(tId)
                .title(title)
                .genres(genres)
                .build();
    }

}
