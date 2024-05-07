package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.service.GenreService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Wrapper;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Movie {

    @Id
    private Long id;

    private String title;

    private Long tId;

    @ManyToMany
    private Set<Genre> genres;

    private Double averageRating;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "movie")
    @JsonBackReference
    public List<Rating> ratings;

    public static Movie toEntity(Long id, Long tId, String title, Set<Genre> genres) {
        return Movie.builder()
                .id(id)
                .tId(tId)
                .title(title)
                .genres(genres)
                .build();
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }

    public void updateAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public void calculateAverageRating() {
        Double averageRating = 0.0;
        log.info(String.valueOf(ratings.size()));
        for (Rating rating : ratings) {
            log.info("평점 아이디 " + (rating.getId()));
            averageRating += rating.getScore();
        }

        if (!ratings.isEmpty()) {
            averageRating /= ratings.size();
        }

        updateAverageRating(averageRating);
    }

}
