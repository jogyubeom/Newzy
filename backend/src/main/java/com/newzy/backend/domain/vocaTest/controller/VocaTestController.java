package com.newzy.backend.domain.vocaTest.controller;

import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDto;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDto;
import com.newzy.backend.domain.vocaTest.service.VocaTestService;
import com.newzy.backend.domain.vocaTest.service.VocaTestServiceImpl;
import com.newzy.backend.global.exception.NotValidRequestException;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    final private UserService userService;

    @GetMapping
    @Operation(summary = "어휘 테스트 목록", description = "어휘 테스트 목록을 조회합니다.")
    public ResponseEntity<List<TestWordListResponseDto>> getTestPage(){
        log.info(">>> [GET] /user/vocabulary-test");
        List<TestWordListResponseDto>  testWordList = vocaTestService.getWordList();

        return ResponseEntity.status(200).body(testWordList);
    }


    @PostMapping
    @Operation(summary = "어휘 테스트 결과" , description = "어휘 테스트 결과를 반환합니다.")
    public ResponseEntity<BaseResponseBody> gradeUserScore(
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid TestResultRequestDto scoreList
    ){
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }

        log.info(">>> [POST] /user/vocabulary-test - 요청 파라미터 userId : {}", userId);

        if (scoreList == null) {
            throw new NotValidRequestException("점수 리스트가 없습니다.");
        }

        vocaTestService.saveUserScore(userId, scoreList);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "유저의 경제, 사회, 세계 점수 등록 완료되었습니다."));

    }
}
