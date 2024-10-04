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
    private String link;
    private String title;
    private String contentText;
    private int category;
    private String publisher;
    private int hit;
    private LocalDateTime createdAt;

}
