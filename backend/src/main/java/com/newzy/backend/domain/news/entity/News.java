package com.newzy.backend.domain.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "news")
public class News extends BaseTimeEntity {
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

    @Column(name = "content_text", columnDefinition = "TEXT", nullable = false)
    private String contentText;

    @Column(name = "difficulty", columnDefinition = "TINYINT", nullable = false)
    private int difficulty;

    @Column(name = "category", columnDefinition = "TINYINT", nullable = false)
    private int category;

    @Column(name = "publisher", length = 100, nullable = false)
    private String publisher;

    @Column(name = "crawled_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime crawledAt = LocalDateTime.now();

    @Column(name = "hit", nullable = false)
    private int hit = 0;

    @Column(name = "like_cnt")
    private int likeCnt = 0;

    @Column(name = "thumbnail", length = 255, nullable = true)
    private String thumbnail;
}
