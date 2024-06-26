package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Rating;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.RatingRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating createRating(Member member, Movie movie, Double score, LocalDateTime createdAt) {
        return Rating.toEntity(member, movie, score, createdAt);
    }

    public Rating findRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.RATING_NOT_FOUND));
    }

    public Double getAverageRatingByMovieId(Long movieId) {
        return ratingRepository.findAverageRatingByMovieId(movieId);
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

}
