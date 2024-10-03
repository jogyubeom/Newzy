package com.newzy.backend.domain.news.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsCardRequestDTO {

    private Long userId;
    private Long newsId;
    private int score;
    private String summary;

}
