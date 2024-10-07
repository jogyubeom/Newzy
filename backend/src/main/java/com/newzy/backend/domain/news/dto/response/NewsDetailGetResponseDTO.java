package com.newzy.backend.domain.news.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsDetailGetResponseDTO {

    private Long newsId;
    private String link;
    private String title;
    private String content;
    private String contentText;
    private int difficulty;
    private int category;
    private String publisher;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder.Default
    private LocalDateTime crawledAt = LocalDateTime.now();
    @Builder.Default
    private int hit = 0;
    private int likeCnt;
    private String thumbnail;
    @JsonProperty("isLiked")
    private boolean isLiked;
    @JsonProperty("isBookmarked")
    private boolean isBookmarked;
    @JsonProperty("isCollected")
    private boolean isCollected;

    public NewsDetailGetResponseDTO(Long newsId, String link, String title, String content, String contentText, int difficulty, int category, String publisher, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime crawledAt, int hit, int likeCnt, String thumbnail) {
        this.newsId = newsId;
        this.link = link;
        this.title = title;
        this.content = content;
        this.contentText = contentText;
        this.difficulty = difficulty;
        this.category = category;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.crawledAt = crawledAt;
        this.hit = hit;
        this.likeCnt = likeCnt;
        this.thumbnail = thumbnail;
        this.updatedAt = updatedAt;
    }
}
