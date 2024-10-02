package com.newzy.backend.domain.vocaTest.controller;

import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDto;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDto;
import com.newzy.backend.domain.vocaTest.service.VocaTestService;
import com.newzy.backend.domain.vocaTest.service.VocaTestServiceImpl;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/vocabulary-test")
public class VocaTestController {

    final private VocaTestService vocaTestService;

    @GetMapping
    @Operation(summary = "어휘 테스트 목록", description = "어휘 테스트 목록을 조회합니다.")
    public ResponseEntity<List<TestWordListResponseDto>> getTestPage(){
        log.info(">>> [GET] /user/vocabulary-test");
        List<TestWordListResponseDto>  testWordList = vocaTestService.getWordList();

        return ResponseEntity.status(200).body(testWordList);
    }

    @PostMapping
    @Operation(summary = "" , description = "")
    public ResponseEntity<BaseResponseBody> gradeUserScore(@RequestBody @Valid TestResultRequestDto scoreList){
        log.info(">>> [POST] /user/vocabulary-test");
        //TODO: requestHeader 로 유저 토큰에서 유저 아이디 뽑아내는 로직 추가
        vocaTestService.saveUserScore(scoreList);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "유저의 경제, 사회, 세계 점수 등록 완료되었습니다."));

    }
}
