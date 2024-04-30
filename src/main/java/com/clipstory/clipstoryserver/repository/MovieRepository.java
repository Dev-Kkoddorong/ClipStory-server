package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Movie;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAll(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword%")
    Page<Movie> findByTitleContainingKeyword(@Param("keyword") String keyword, Pageable pageable);

}
