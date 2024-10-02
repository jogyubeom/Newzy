package com.newzy.backend.domain.vocaTest.dto.response;

import com.newzy.backend.domain.vocaTest.entity.TestWord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(title = "TEST_WORD_LIST_RESP : 어휘 테스트 DTO")
public class TestWordListResponseDto {

    private int category;
    private Long wordId;
    private String word;

    public static TestWordListResponseDto convertToDto(TestWord testWord) {
        if (testWord == null){
            throw new IllegalArgumentException("테스트 어휘 엔티티가 없습니다.");
        }

        return TestWordListResponseDto.builder()
                .category(testWord.getCategory())
                .wordId(testWord.getWordId())
                .word(testWord.getWord())
                .build();
    }

}
