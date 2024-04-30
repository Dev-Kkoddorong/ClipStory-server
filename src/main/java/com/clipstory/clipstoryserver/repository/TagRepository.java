package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByMovieId(Long movieId);

}
