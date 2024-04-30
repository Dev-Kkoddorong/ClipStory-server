package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.repository.GenreRepository;
import com.clipstory.clipstoryserver.responseDto.GenreResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre findOrCreateNew(String name) {
        return genreRepository.findByName(name).orElseGet(
                () -> genreRepository.save(Genre.toEntity(name))
        );
    }

    public List<GenreResponseDto> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genre -> GenreResponseDto.toGenreResponseDto(genre))
                .collect(Collectors.toList());
    }

}
