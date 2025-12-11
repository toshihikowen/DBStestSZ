package com.example.githubrepo.service;

import com.example.githubrepo.github.GitHubClient;
import com.example.githubrepo.github.GitHubRepoDto;
import com.example.githubrepo.model.RepositoryCache;
import com.example.githubrepo.repo.RepositoryCacheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RepositoryService {
    private final RepositoryCacheRepository repository;
    private final GitHubClient client;

    public RepositoryService(RepositoryCacheRepository repository, GitHubClient client) {
        this.repository = repository;
        this.client = client;
    }

    public RepositoryResponse getRepository(String owner, String name) {
        return repository.findByOwnerAndName(owner, name)
                .map(c -> new RepositoryResponse(c.getFullName(), c.getDescription(), c.getCloneUrl(), c.getStars(), c.getCreatedAt()))
                .orElseGet(() -> fetchAndCache(owner, name));
    }

    private RepositoryResponse fetchAndCache(String owner, String name) {
        GitHubRepoDto dto = client.getRepository(owner, name);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RepositoryCache c = new RepositoryCache(owner, name, dto.getFullName(), dto.getDescription(), dto.getCloneUrl(), dto.getStargazersCount(), dto.getCreatedAt());
        RepositoryCache saved = repository.save(c);
        return new RepositoryResponse(saved.getFullName(), saved.getDescription(), saved.getCloneUrl(), saved.getStars(), saved.getCreatedAt());
    }
}
