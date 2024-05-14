package com.clipstory.clipstoryserver.domain;

import com.clipstory.clipstoryserver.requestDto.MemberRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    private String customId;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

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
