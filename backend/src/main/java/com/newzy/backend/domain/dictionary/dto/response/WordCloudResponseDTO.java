package com.newzy.backend.domain.dictionary.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordCloudResponseDTO {
    private String text;
    private int value;
    private int category;
}