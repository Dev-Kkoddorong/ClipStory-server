package com.clipstory.clipstoryserver.domain;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    private String customId;

    private String password;

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

    public static Member toEntity(String customId, String name, String password) {
        return Member.builder()
                .customId(customId)
                .name(name)
                .password(password)
                .build();
    }

    public static Member toEntity(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

}
