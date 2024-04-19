package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import com.clipstory.clipstoryserver.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public void createTag(Member member, Movie movie, String content, LocalDateTime createdAt) {
        Tag tag = Tag.toEntity(member, movie, content, createdAt);
        tagRepository.save(tag);
    }

    public List<Tag> getTagsByMovieId(Long movieId) {
        return tagRepository.findAllByMovieId(movieId);
    }

}
