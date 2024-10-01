package com.newzy.backend.domain.news.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsCardRequestDTO {

    // TODO : 추후 userId 추가해서 다시 수정
//    private Long userId;
    private Long newsId;
    private int score;
    private String summary;

}
