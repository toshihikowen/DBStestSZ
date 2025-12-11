package com.example.githubrepo.web;

import com.example.githubrepo.service.RepositoryResponse;
import com.example.githubrepo.service.RepositoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {
    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * Get repository details
     * @param owner
     * @param repositoryName
     * @return
     */
    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryService.getRepository(owner, repositoryName);
    }
}
