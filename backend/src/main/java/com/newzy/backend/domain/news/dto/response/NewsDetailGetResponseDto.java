package com.newzy.backend.domain.news.dto.response;

import com.newzy.backend.domain.news.entity.News;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsDetailGetResponseDto {

    private Long newsId;
    private String link;
    private String title;
    private String content;
    private int difficulty;
    private int category;
    private String publisher;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime crawledAt = LocalDateTime.now();
    private int hit = 0;
    private String thumbnail;


    public static NewsDetailGetResponseDto convertToDTO(News news) {
        NewsDetailGetResponseDto dto = new NewsDetailGetResponseDto();
        dto.setNewsId(news.getNewsId());
        dto.setLink(news.getLink());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setDifficulty(news.getDifficulty());
        dto.setCategory(news.getCategory());
        dto.setPublisher(news.getPublisher());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        dto.setHit(news.getHit());
        dto.setThumbnail(news.getThumbnail());
        return dto;
    }

}
