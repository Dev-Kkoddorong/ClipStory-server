package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.requestDto.MemberRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String customId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "member")
    @JsonBackReference
    @Builder.Default
    private List<Tag> tagList = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "member")
    @JsonBackReference
    @Builder.Default
    private List<Rating> ratingList = new ArrayList<>();

    public void addTag(Tag tag) {
        tagList.add(tag);
    }

    public void addRating(Rating rating) {
        ratingList.add(rating);
    }

    public Movie getBestMovie() {
        AtomicReference<Rating> maxRating = new AtomicReference<>(null);

        ratingList.parallelStream().forEach(rating -> {
            if (maxRating.get() == null || rating.getScore() > maxRating.get().getScore()) {
                maxRating.set(rating);
            }
        });

        return maxRating.get().getMovie();
    }

    public Member(String customId, String password, Role role) {
        this.customId = customId;
        this.password = password;
        this.role = role;
    }

    public static Member toEntity(String customId, String name, String password, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .customId(customId)
                .name(name)
                .role(Role.USER)
                .password(passwordEncoder.encode(password))
                .build();
    }

    public static Member toEntity(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

    public static Member toEntity(MemberRequestDto memberRequestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .customId(memberRequestDto.getCustomId())
                .name(memberRequestDto.getName())
                .role(Role.USER)
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().getKey()));
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
