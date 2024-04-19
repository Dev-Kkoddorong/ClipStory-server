package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(Long movieId);

}
