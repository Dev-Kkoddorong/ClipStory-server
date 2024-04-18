package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.repository.InitRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;

@RequiredArgsConstructor
@Service
public class InitService {

    private final InitRepository initRepository;

    private final GenreService genreService;

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

            StringBuilder title = new StringBuilder();
            for(int i = 1; i < token.length - 1; i++) {
                title.append(token[i]);
                if(i != token.length-2) title.append(",");
            }

            Long tId = MovieIdToTid.get(movieId);
            Movie movie = Movie.toEntity(movieId, tId, title, genre, genreService);
            initRepository.save(movie);
        }

    }

}
