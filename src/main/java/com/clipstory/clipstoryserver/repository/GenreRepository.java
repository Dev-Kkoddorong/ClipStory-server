package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

}
