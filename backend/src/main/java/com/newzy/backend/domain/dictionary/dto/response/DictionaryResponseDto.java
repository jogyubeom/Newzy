package com.newzy.backend.domain.dictionary.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryResponseDto {
    private String id;
    private String word;
    private String definition;
}
