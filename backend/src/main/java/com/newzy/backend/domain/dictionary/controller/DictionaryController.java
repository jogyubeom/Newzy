package com.newzy.backend.domain.dictionary.controller;

import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDto;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDto;
import com.newzy.backend.domain.dictionary.service.DictionaryService;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.exception.NotValidRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/word")
@Tag(name = "어휘 검색 API", description = "Dictionary 관련 API")
public class DictionaryController {
    private final UserService userService;
    private final DictionaryService dictionaryService;

    @GetMapping("/{newsId}")
    @Operation(summary = "어휘 검색 정보 조회", description = "입력으로 주어진 word의 검색 결과를 반환합니다.")
    public ResponseEntity<List<DictionaryResponseDto>> searchByWord(
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "뉴스 ID", required = true)
            @PathVariable Long newsId,
            @Parameter(description = "검색 단어", required = true, example = "나무")
            @RequestParam(value = "search") String search) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }

        log.info(">>> [GET] /word/{}?search={} - 요청 파라미터: userId:{}, newsId:{}, search:{}", newsId, search, userId, newsId, search);

        if (search == null || search.isEmpty())
            throw new NotValidRequestException("검색 단어가 없습니다.");
        if (newsId == null || newsId <= 0)
            throw new NotValidRequestException("뉴스 ID: " + newsId + " 값이 유효하지 않습니다.");

        List<DictionaryResponseDto> dictionaryList = dictionaryService.searchByWord(search);

        if (userId != 0) { // 사용자의 나만의 단어장에 저장
            dictionaryService.addSearchWordHistory(userId, newsId, search);
        }
        log.info(">>> 단어 검색 결과 개수: {}", dictionaryList.size());
        return ResponseEntity.status(200).body(dictionaryList);
    }

    @GetMapping("/wordcloud")
    @Operation(summary = "WordCloud 데이터 조회", description = "최근 3일동안 검색한 단어를 반환합니다.")
    public ResponseEntity<List<WordCloudResponseDto>> getWordCloudHistory() {
        log.info(">>> [GET] /word/wordcloud - 요청 파라미터 없음");
        List<WordCloudResponseDto> wordCloudResponseDtoList = dictionaryService.getWordCloudHistory();
        return ResponseEntity.status(200).body(wordCloudResponseDtoList);
    }
}
