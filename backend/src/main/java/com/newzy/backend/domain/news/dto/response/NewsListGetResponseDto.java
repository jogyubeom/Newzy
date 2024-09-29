package com.newzy.backend.domain.news.dto.response;

import com.newzy.backend.domain.news.entity.News;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsListGetResponseDto {
    private Long newsId;
    private String title;
    private String content;
    private int category;
    private String publisher;
    private int hit;
    private LocalDateTime createdAt;

    public static NewsListGetResponseDto convertToDTO(News news){
        if (news == null){
            throw new IllegalArgumentException("뉴스 엔티티가 없습니다.");
        }
        return NewsListGetResponseDto.builder()
                .newsId(news.getNewsId())
                .title(news.getTitle())
                .content(news.getContent())
                .category(news.getCategory())
                .publisher(news.getPublisher())
                .hit(news.getHit())
                .createdAt(news.getCreatedAt())
                .build();
    }
}
