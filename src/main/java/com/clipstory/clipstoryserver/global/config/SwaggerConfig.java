package com.clipstory.clipstoryserver.global.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springBootAPI() {

        Info info = new Info()
                .title("SpringBoot Rest API Documentation")
                .description("2024년도 소프트웨어공학 과목 Dev-Kkoddorong 팀 ClipStory 프로젝트 BE api")
                .contact(new Contact().name("전민주").url("https://github.com/mingmingmon").email("mingmingmon@kyonggi.ac.kr"))
                .version("0.1");

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info);
    }

}
