package com.newzy.backend.domain.vocaTest.service;

import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDto;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface VocaTestService {

    void saveUserScore(Long userId, TestResultRequestDto saveUserScore); // 괄호 수정

    List<TestWordListResponseDto> getWordList();}
