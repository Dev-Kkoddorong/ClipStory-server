package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
