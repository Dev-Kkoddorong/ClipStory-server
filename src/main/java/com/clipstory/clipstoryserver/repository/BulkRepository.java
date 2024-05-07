package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Rating;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllMovies(List<Movie> movieList) {
        String insertMovieSQL = "INSERT INTO movie (id, title, t_id)" +
                "VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(insertMovieSQL,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Movie movie = movieList.get(i);
                        ps.setLong(1, movie.getId());
                        ps.setString(2, movie.getTitle());
                        if (movie.getTId() != null) {
                            ps.setLong(3, movie.getTId());
                        } else {
                            ps.setNull(3, Types.BIGINT);
                        }

                    }
                    @Override
                    public int getBatchSize() {
                        return movieList.size();
                    }
                });

       // asdf(movieList);


    }

    @Transactional
    public void asdf (List<Movie> movieList) {


        String insertMovieGenreSQL = "INSERT INTO movie_genres (genres_id, movie_id)" +
                "VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertMovieGenreSQL,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Movie movie = movieList.get(i);
                        Long movieId = movie.getId();
                        Set<Genre> genres = movie.getGenres();

                 /*       int size = 0;
                        for(Movie m : movieList) {
                            size += m.getGenres().size();
                        }
                        log.info(String.valueOf(size));
*/
                        Iterator<Genre> iterSet = genres.iterator();
                        while(iterSet.hasNext()) {
                            Genre g = iterSet.next();
                            ps.setLong(1, g.getId());
                            ps.setLong(2, movieId);

                            log.info(movieId + " " + g.getName());
                        }
                        //ps.addBatch();
/*
                        ps.execute();
                        log.info("실행함");
                        ps.clearBatch();
                        log.info("지움");*/

                    }

                    @Override
                    public int getBatchSize() {
                        return movieList.size();
                    }
                });
    }

    @Transactional
    public void saveAllRatings(List<Rating> ratingList) {
        String sql = "INSERT INTO rating (member_id, movie_id, score, created_at)" +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Rating rating = ratingList.get(i);
                        ps.setLong(1, rating.getMember().getId());
                        ps.setLong(2, rating.getMovie().getId());
                        ps.setDouble(3, rating.getScore());
                        ps.setObject(4, rating.getCreatedAt());
                    }

                    @Override
                    public int getBatchSize() {
                        return ratingList.size();
                    }
                });
    }

    @Transactional
    public void saveAllAverageRatingRatings(List<Movie> movieList) {
        String sql = "UPDATE table_name SET average_rating = ?, WHERE id = ?";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Movie movie = movieList.get(i);
                        ps.setDouble(1, movie.getAverageRating());
                        ps.setLong(2, movie.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return movieList.size();
                    }
                });
    }

}
