package com.newzy.backend.domain.news.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsCardInfoGetResponseDTO {
    private Long cardId;
    private Long userId;
    private String userNickname;
    private Long newsId;
    private String newsTitle;
    private String summary;
    private int category;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private int score;
}
