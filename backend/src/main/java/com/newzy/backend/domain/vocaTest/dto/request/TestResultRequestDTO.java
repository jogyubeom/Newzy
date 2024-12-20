package com.newzy.backend.domain.vocaTest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "TEST_RES_REQ : 어휘 테스트 결과 DTO")
public class TestResultRequestDTO {

    private Map<Integer, Integer> categoryScores;
    private String nickname;
    private LocalDate birth;
    private String info;
}
