package com.clipstory.clipstoryserver.global.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class TidConfig {

    @Bean
    public Map<Long, Long> MovieIdToTid() throws IOException {
        Map<Long, Long> movieIdToTid = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("static/links.csv");
        File linksCsv = resource.getFile();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new FileReader(linksCsv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] token = line.split(",");

            if(token.length > 2) {
                Long movieId = Long.parseLong(token[0]);
                Long tId = Long.parseLong(token[2]);
                movieIdToTid.put(movieId, tId);
            }

        }
        return movieIdToTid;
    }
}
