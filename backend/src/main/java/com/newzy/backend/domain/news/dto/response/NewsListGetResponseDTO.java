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
public class NewsListGetResponseDTO {
    private Long newsId;
    private String link;
    private String title;
    private String contentText;
    private int category;
    private String publisher;
    private String thumbnail;
    private int hit;
    private int likeCnt;
    private LocalDateTime createdAt;

    public static NewsListGetResponseDTO convertToDTO(News news){
        if (news == null){
            throw new IllegalArgumentException("뉴스 엔티티가 없습니다.");
        }
        return NewsListGetResponseDTO.builder()
                .newsId(news.getNewsId())
                .link(news.getLink())
                .title(news.getTitle())
                .contentText(news.getContentText())
                .category(news.getCategory())
                .publisher(news.getPublisher())
                .thumbnail(news.getThumbnail())
                .hit(news.getHit())
                .likeCnt(news.getLikeCnt())
                .createdAt(news.getCreatedAt())
                .build();
    }
}
