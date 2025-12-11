package com.example.githubrepo.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component  // Registers this class as a Spring-managed bean so other parts of the app can use it
public class GitHubClient {
    private final RestTemplate restTemplate;  // Holds the HTTP client used to perform web requests
    private final String apiBaseUrl;    // Stores the base web address for the GitHub API (e.g., https://api.github.com)

    public GitHubClient(RestTemplate restTemplate, @Value("${github.apiBaseUrl:https://api.github.com}") String apiBaseUrl) { // Builds a GitHubClient with a RestTemplate and a configurable API base URL
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Makes a web request to the GitHub API to retrieve a repository's details.
     *
     * @param owner The owner of the repository (e.g., "octocat" for https://github.com/octocat/Spoon-Knife)
     * @param name  The name of the repository (e.g., "Spoon-Knife" for https://github.com/octocat/Spoon-Knife)
     * @return A GitHubRepoDto object containing the repository's details, or null if the repository was not found
     */
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
