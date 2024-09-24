package com.newzy.backend.domain.news.dto.response;

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
}
