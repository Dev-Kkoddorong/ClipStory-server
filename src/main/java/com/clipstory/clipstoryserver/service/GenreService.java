package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.repository.GenreRepository;
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

}
