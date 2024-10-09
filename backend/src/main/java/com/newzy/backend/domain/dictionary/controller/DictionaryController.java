package com.newzy.backend.domain.dictionary.controller;

import com.newzy.backend.domain.dictionary.dto.request.SearchWordRequestDTO;
import com.newzy.backend.domain.dictionary.dto.request.VocaListRequestDTO;
import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.VocaListResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDTO;
import com.newzy.backend.domain.dictionary.service.DictionaryService;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.exception.NotValidRequestException;
import com.newzy.backend.global.model.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/word")
@Tag(name = "어휘 검색 API", description = "Dictionary 관련 API")
public class DictionaryController {
    private final UserService userService;
    private final DictionaryService dictionaryService;

    @GetMapping("/search")
    @Operation(summary = "어휘 검색 정보 조회", description = "입력으로 주어진 word의 검색 결과를 반환합니다.")
    public ResponseEntity<List<DictionaryResponseDTO>> searchByWord(
            @Parameter(description = "JWT")
            @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "뉴스 카테고리 (0: 경제, 1: 사회, 2: 세계, 3: 뉴지)", required = true)
            @RequestParam(value = "category") int category,
            @Parameter(description = "검색 단어", required = true, example = "나무")
            @RequestParam(value = "word") String word) {

        log.info(">>> [GET] /word/search?category={}&word={} - 요청 파라미터: category:{}, word:{}", category, word, category, word);

        if (word == null || word.isEmpty())
            throw new NotValidRequestException("검색 단어가 없습니다.");
        if (category < 0 || category > 3)
            throw new NotValidRequestException("카테고리: " + category + " 값이 유효하지 않습니다.");

        List<DictionaryResponseDTO> dictionaryList = dictionaryService.searchByWord(word);

        // 검색한 어휘 Redis 에 저장
        dictionaryService.saveSearchWordHistoryToRedis(category, word, token);

        return ResponseEntity.status(200).body(dictionaryList);
    }

    @PostMapping
    @Operation(summary = "사용자의 나만의 단어장에 어휘 추가", description = "나만의 단어장에 어휘를 저장합니다.")
    public ResponseEntity<BaseResponseBody> saveSearchWord (
            @Parameter(description = "JWT")
            @RequestHeader(value = "Authorization") String token,
            @RequestBody @Validated VocaListRequestDTO vocaListRequestDTO
            ) {
        Long userId = 0L;
        if (token != null) {
            userId = userService.getUser(token).getUserId();
        }

        log.info(">>> [POST] /word - 요청 파라미터: userId:{}, dto: {}", userId, vocaListRequestDTO);

        dictionaryService.addVocaList(userId, vocaListRequestDTO);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "나만의 단어장에 저장이 완료되었습니다."));
    }
    @GetMapping
    @Operation(summary = "사용자의 어휘 검색 기록 조회", description = "사용자가 검색한 어휘를 조회합니다.")
    public ResponseEntity<Map<String, Object>> getSearchWordHistory(
            @Parameter(description = "JWT")
            @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "페이지 번호 (default: 0)")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "정렬 기준 (0: 최신순, 1: 오래된 순)")
            @RequestParam(value = "sort", required = false, defaultValue = "0") int sort
    ) {
        log.info(">>> [GET] /word?page={}&sort={} - 요청 파라미터: page={}, sort={}", page, sort, page, sort);
        Long userId = userService.getUser(token).getUserId();
        SearchWordRequestDTO searchWordRequestDTO = SearchWordRequestDTO
                .builder()
                .userId(userId)
                .page(page)
                .sort(sort)
                .build();

        Map<String, Object> dictionaryResponseDTOList = dictionaryService.getVocaList(searchWordRequestDTO);
        return ResponseEntity.status(200).body(dictionaryResponseDTOList);
    }


    @DeleteMapping
    @Operation(summary = "사용자의 어휘 검색 기록 삭제", description = "사용자가 검색한 어휘를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteSearchWordHistory(
            @Parameter(description = "JWT")
            @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "사용자가 검색한 어휘 목록 - path: /api/word?wordList=하이, 바이, 크크 / 중간에 띄워쓰기 필수")
            @RequestParam(value = "wordList") List<String> wordList
    ) {
        log.info(">>> [DELETE] /word?wordList={} - 요청 파라미터: wordList: {}", wordList, wordList);
        if (wordList == null || wordList.isEmpty() || wordList.size() == 0)
            throw new NotValidRequestException("삭제할 단어가 없습니다.");

        Long userId = userService.getUser(token).getUserId();

        dictionaryService.deleteSearchWordHistory(userId, wordList);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 어휘 검색 기록 삭제가 완료되었습니다."));
    }

    @GetMapping("/wordcloud")
    @Operation(summary = "WordCloud 데이터 조회", description = "최근 3일동안 검색한 단어를 반환합니다.")
    public ResponseEntity<List<WordCloudResponseDTO>> getWordCloudHistory() {
        log.info(">>> [GET] /word/wordcloud - 요청 파라미터 없음");
        List<WordCloudResponseDTO> wordCloudResponseDtoList = dictionaryService.getWordCloudHistory();
        return ResponseEntity.status(200).body(wordCloudResponseDtoList);
    }
}
