package com.newzy.backend.domain.news.controller;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Tag(name = "News API", description = "News 관련 API")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/{newsId}")
    @Operation(summary = "뉴스 정보 조회", description = "식물 ID로 식물의 상세 정보를 조회합니다.")
    public ResponseEntity<NewsDetailGetResponseDto> getNews(
            @Parameter(description = "뉴스 ID", required = true) @PathVariable Long newsId) {
        log.info(">>> [GET] /news/{} - 요청 ID: {}", newsId, newsId);

        NewsDetailGetResponseDto newsDetailGetResponseDto = newsService.getNewsDetail(newsId);
        return ResponseEntity.status(200).body(newsDetailGetResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<NewsListGetResponseDto>> getNewsList(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "카테고리") @RequestParam(value = "category", required = false, defaultValue = "3") int category) {
        log.info(">>> [GET] /news - 요청 파라미터: page - {}, category - {}", page, category);
        List<NewsListGetResponseDto> newsListGetResponseDtoList = newsService.getNewsList(page, category);
        return ResponseEntity.status(200).body(newsListGetResponseDtoList);
    }
}
