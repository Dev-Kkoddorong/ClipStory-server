package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.service.GenreService;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToMany;


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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Genre> genres;

    private Double averageRating;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "movie")
    @JsonBackReference
    private List<Rating> ratings;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "movie")
    @JsonBackReference
    private List<Tag> tags;

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

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void updateAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Movie calculateAverageRating() {
        Double averageRating = 0.0;
        for (Rating rating : ratings) {
            averageRating += rating.getScore();
        }

        if (!ratings.isEmpty()) {
            averageRating /= ratings.size();
        }

        updateAverageRating(averageRating);
        return this;
    }

}
