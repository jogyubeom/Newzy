package com.newzy.backend.domain.dictionary.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocaListResponseDTO {
    private String word;
    private String definition;
}
