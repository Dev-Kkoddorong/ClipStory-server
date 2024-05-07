package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.clipstory.clipstoryserver.domain.Rating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;

@RequiredArgsConstructor
@Service
@Slf4j
public class InitService {

    private final MovieService movieService;

    private final GenreService genreService;

    private final MemberService memberService;

    private final TagService tagService;

    private final RatingService ratingService;

    private final Map<Long, Long> MovieIdToTid;

    public void addMovies() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/movies.csv");
        File moviesCsv = resource.getFile();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new FileReader(moviesCsv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] token = line.split(",");
            Long movieId = Long.parseLong(token[0]);
            String[] genre = token[token.length - 1].split("\\|");

            StringBuilder titleBuilder = new StringBuilder();
            for(int i = 1; i < token.length - 1; i++) {
                titleBuilder.append(token[i]);
                if(i != token.length-2) titleBuilder.append(",");
            }

            String title = titleBuilder.toString();
            Long tId = MovieIdToTid.get(movieId);
            Set<Genre> genres = Arrays.stream(genre)
                    .map(genreService::findOrCreateNew)
                    .collect(Collectors.toSet());

            movieService.createMovie(movieId, tId, title, genres);
        }
    }

    public void addTags() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/tags.csv");
        File moviesCsv = resource.getFile();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new FileReader(moviesCsv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] token = line.split(",");
            String memberCustomId = token[0];
            Long movieId = Long.parseLong(token[1]);
            String tagContent = token[2];
            Long timeStamp = Long.parseLong(token[3]);
            Instant instant = Instant.ofEpochSecond(timeStamp);
            LocalDateTime createdAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            memberService.findOrCreateMember(memberCustomId);

            Member member = memberService.findMemberByCustomId(memberCustomId);
            Movie movie = null;
            movie = movieService.findMovieById(movieId);


            tagService.createTag(member, movie, tagContent, createdAt);
        }
    }

    public void addRatings() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/ratings0.csv");
        File moviesCsv = resource.getFile();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new FileReader(moviesCsv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] token = line.split(",");
            String memberCustomId = token[0];
            Long movieId = Long.parseLong(token[1]);
            Double score = Double.parseDouble(token[2]);
            Long timeStamp = Long.parseLong(token[3]);
            Instant instant = Instant.ofEpochSecond(timeStamp);
            LocalDateTime createdAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            Member member = memberService.findOrCreateMember(memberCustomId);
            Movie movie = movieService.findMovieById(movieId);

            Rating rating =  ratingService.createRating(member, movie, score, createdAt);
            movie.addRating(rating);
        }

        for (Movie movie : movieService.findAllMovies()) {
            log.info(String.valueOf(movie.getRatings().size()));
            movie.calculateAverageRating();
            movieService.updateMovie(movie);
        }
    }

}
