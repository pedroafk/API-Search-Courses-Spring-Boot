package com.searchcourses.api.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Search Courses API")
                        .version("1.0.0")
                        .description("API for searching courses from various sites."))
                        .servers(Collections.singletonList(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("Local server")
                        ));
    }
}
