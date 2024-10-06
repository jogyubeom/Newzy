package com.newzy.backend.domain.vocaTest.service;

import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDTO;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDTO;

import java.util.List;

public interface VocaTestService {

    void saveUserScore(Long userId, TestResultRequestDTO saveUserScore); // 괄호 수정

    List<TestWordListResponseDTO> getWordList();
}
