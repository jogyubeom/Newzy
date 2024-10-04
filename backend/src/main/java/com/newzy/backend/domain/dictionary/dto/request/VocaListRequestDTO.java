package com.newzy.backend.domain.dictionary.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocaListRequestDTO {
    @NotNull(message = "단어는 필수 필드입니다.")
    private String word;
    @NotNull(message = "단어의 뜻은 필수 필드입니다.")
    private String definition;
}
