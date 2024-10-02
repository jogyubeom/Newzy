package com.newzy.backend.domain.dictionary.controller;

import com.newzy.backend.domain.dictionary.dto.request.SearchWordRequestDTO;
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
    public ResponseEntity<List<DictionaryResponseDTO>> searchByWord(
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

        List<DictionaryResponseDTO> dictionaryList = dictionaryService.searchByWord(search);

        // 검색한 어휘 Redis 에 저장
        dictionaryService.saveSearchWordHistoryToRedis(newsId, search);
        // 사용자의 단어장에 저장
        if (userId != 0)
            dictionaryService.addSearchWordHistory(userId, newsId, search);

        log.info(">>> 단어 검색 결과 개수: {}", dictionaryList.size());
        return ResponseEntity.status(200).body(dictionaryList);
    }

    @GetMapping
    @Operation(summary = "사용자의 어휘 검색 기록 조회", description = "사용자가 검색한 어휘를 조회합니다.")
    public ResponseEntity<List<VocaListResponseDTO>> getSearchWordHistory(
            @Parameter(description = "JWT", required = false)
            @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "페이지 번호")
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

        List<VocaListResponseDTO> dictionaryResponseDTOList = dictionaryService.getSearchWordHistory(searchWordRequestDTO);
        return ResponseEntity.status(200).body(dictionaryResponseDTOList);
    }

    @DeleteMapping
    @Operation(summary = "사용자의 어휘 검색 기록 삭제", description = "사용자가 검색한 어휘를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteSearchWordHistory(
            @Parameter(description = "JWT")
            @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "사용자가 검색한 어휘")
            @RequestParam(value = "word", defaultValue = "0") String word
    ) {
        log.info(">>> [DELETE] /word/{} - 요청 파라미터: wordId={}", word, word);
        if (word == null || word.isEmpty())
            throw new NotValidRequestException("삭제할 단어가 없습니다.");
        Long userId = userService.getUser(token).getUserId();

        dictionaryService.deleteSearchWordHistory(userId, word);

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
