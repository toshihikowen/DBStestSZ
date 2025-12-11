package com.example.githubrepo.repo;

import com.example.githubrepo.model.RepositoryCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryCacheRepository extends JpaRepository<RepositoryCache, Long> {
    Optional<RepositoryCache> findByOwnerAndName(String owner, String name);
}
