package com.clipstory.clipstoryserver.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JsonBackReference
    private Movie movie;

    private Double score;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Rating toEntity(Member member, Movie movie, Double score, LocalDateTime createdAt) {
        return Rating.builder()
                .member(member)
                .movie(movie)
                .score(score)
                .createdAt(createdAt)
                .build();
    }

}
