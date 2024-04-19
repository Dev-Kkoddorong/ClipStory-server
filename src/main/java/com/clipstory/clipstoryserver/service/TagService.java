package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.List;
import com.clipstory.clipstoryserver.responseDto.TagResponseDto;
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

    public TagResponseDto getTag(Long tagId) {
        Tag tag = getTagById(tagId);
        return TagResponseDto.toResponseDto(tag);
    }

    public Tag getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new GeneralException(Status.TAG_NOT_FOUND));
    }

    public List<TagResponseDto> getAllTag() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> TagResponseDto.toResponseDto(tag))
                .toList();
    }

    public List<Tag> getTagsByMovieId(Long movieId) {
        return tagRepository.findAllByMovieId(movieId);
    }

}
