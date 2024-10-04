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
    private String thumbnail;

}
