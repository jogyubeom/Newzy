package com.newzy.backend.domain.newzy.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewzyListGetRequestDTO {
    private int page;
    private int category;
    private int sort;
    private String keyword;
}
