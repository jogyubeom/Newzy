package com.newzy.backend.domain.dictionary.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordCloudResponseDto {
    private String word;
    private int count;
}
