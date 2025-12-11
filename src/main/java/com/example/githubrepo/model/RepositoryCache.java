package com.example.githubrepo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repository_cache", uniqueConstraints = @UniqueConstraint(columnNames = {"owner", "name"}))
public class RepositoryCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String cloneUrl;

    @Column(nullable = false)
    private int stars;

    @Column(nullable = false)
    private String createdAt;

    public RepositoryCache() {}

    public RepositoryCache(String owner, String name, String fullName, String description, String cloneUrl, int stars, String createdAt) {
        this.owner = owner;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
