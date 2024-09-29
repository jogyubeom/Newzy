package com.newzy.backend.domain.news.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
