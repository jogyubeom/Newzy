package com.newzy.backend.domain.news.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsListGetRequestDTO {
    private int page;
    private int category;
    private int sort;
    private String keyword;
}
