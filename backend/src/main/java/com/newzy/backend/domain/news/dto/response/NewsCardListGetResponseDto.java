package com.newzy.backend.domain.news.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewsCardListGetResponseDto {
    private Long cardId;
    private Long userId;
    private String userNickname;
    private Long newsId;
    private String newsTitle;
    private String summary;
    private int category;
    private String thumbnailUrl;

}
