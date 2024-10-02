package com.newzy.backend.domain.dictionary.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryResponseDTO {
    private String id;
    private String word;
    private String definition;
}
