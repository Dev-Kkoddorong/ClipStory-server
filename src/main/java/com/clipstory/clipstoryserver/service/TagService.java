package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Tag;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.TagRepository;
import com.clipstory.clipstoryserver.responseDto.PagedResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import com.clipstory.clipstoryserver.responseDto.TagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public PagedResponseDto<TagResponseDto> getAllTag(Pageable pageable) {
        Page<Tag> tags = tagRepository.findAll(pageable);
        return new PagedResponseDto<>(tags.map(tag -> TagResponseDto.toResponseDto(tag)));
    }

    public List<Tag> getTagsByMovieId(Long movieId) {
        return tagRepository.findAllByMovieId(movieId);
    }

}
