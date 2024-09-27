package com.newzy.backend.domain.news.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "link", length = 255, unique = true, nullable = false)
    private String link;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "difficulty", columnDefinition = "TINYINT", nullable = false)
    private int difficulty;

    @Column(name = "category", columnDefinition = "TINYINT", nullable = false)
    private int category;

    @Column(name = "publisher", length = 100, nullable = false)
    private String publisher;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "crawled_at", updatable = false)
    private LocalDateTime crawledAt = LocalDateTime.now();

    @Column(name = "hit", nullable = false)
    private int hit = 0;

    @Column(name = "thumbnail", length = 255, nullable = true)
    private String thumbnail;
}
