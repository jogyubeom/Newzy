package com.newzy.backend.domain.dictionary.dto.request;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchWordRequestDTO {
    private Long userId;
    private Long newsId;
    private int page;
    private int sort;
}
