package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
