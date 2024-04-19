package com.clipstory.clipstoryserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Tag> tagList;

    @OneToMany(mappedBy = "member")
    private List<Rating> ratingList;

    public void addTag(Tag tag) {
        this.tagList.add(tag);
    }

    public void addRating(Rating rating) {
        this.ratingList.add(rating);
    }

    public static Member toEntity(String customId, String name) {
        return Member.builder()
                .customId(customId)
                .name(name)
                .build();
    }

    public static Member toEntity(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

}
