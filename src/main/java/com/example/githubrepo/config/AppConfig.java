package com.example.githubrepo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration  // Marks this class as a source of bean definitions managed by the Spring container
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        // Returns the HTTP client object that other parts of the app will use
        return new RestTemplate();
    }
}
