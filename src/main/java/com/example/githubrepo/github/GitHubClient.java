package com.example.githubrepo.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubClient {
    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    public GitHubClient(RestTemplate restTemplate, @Value("${github.apiBaseUrl:https://api.github.com}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    public GitHubRepoDto getRepository(String owner, String name) {
        String url = apiBaseUrl + "/repos/" + owner + "/" + name;
        try {
            return restTemplate.getForObject(url, GitHubRepoDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw e;
        }
    }
}
