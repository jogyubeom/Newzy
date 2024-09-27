package com.newzy.backend.domain.news.controller;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.service.NewsService;
import com.newzy.backend.domain.news.service.NewsServiceImpl;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Tag(name = "News API", description = "News 관련 API")
public class NewsController {
    private final NewsServiceImpl newsServiceImpl;

    @GetMapping("/{newsId}")
    @Operation(summary = "뉴스 정보 조회", description = "뉴스 ID로 뉴스의 상세 정보를 조회합니다.")
    public ResponseEntity<NewsDetailGetResponseDto> getNews(
            @Parameter(description = "뉴스 ID", required = true)
            @PathVariable Long newsId) {

        log.info(">>> [GET] /news/{} - 요청 ID: {}", newsId, newsId);

        NewsDetailGetResponseDto newsDetailGetResponseDto = newsServiceImpl.getNewsDetail(newsId);
        return ResponseEntity.status(200).body(newsDetailGetResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<NewsListGetResponseDto>> getNewsList(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "카테고리")
            @RequestParam(value = "category", required = false, defaultValue = "3") int category) {
        log.info(">>> [GET] /news - 요청 파라미터: page - {}, category - {}", page, category);

        List<NewsListGetResponseDto> newsListGetResponseDtoList = newsServiceImpl.getNewsList(page, category);
        return ResponseEntity.status(200).body(newsListGetResponseDtoList);
    }

    @PostMapping(value = "/news-bookmark/{newsId}")
    @Operation(summary = "뉴스 북마크 등록", description = "해당 뉴스를 북마크합니다.")
    public ResponseEntity<BaseResponseBody> bookmarkNews (
            @PathVariable("newsId") Long newsId){
        log.info(">>> [POST] /newsController bookmarkNews - 요청 파라미터: newsId - {}", newsId);
        newsServiceImpl.bookmark(newsId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴스를 북마크했습니다."));
    }

    @DeleteMapping(value = "/news-bookmark/{newsId}/{bookmarkId}")
    @Operation(summary = "뉴스 북마크 삭제", description = "해당 뉴스 북마크를 삭제합니다.")
    public ResponseEntity<BaseResponseBody> deleteBookmark (
            @PathVariable("newsId") Long newsId,
            @PathVariable("bookmarkId") Long bookmarkId
    ){
        log.info(">>> [DELETE] /newsController deleteBookmark - 요청 파라미터: newsId - {}, bookmarkId - {}", newsId, bookmarkId);
        newsServiceImpl.deleteBookmark(bookmarkId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴스를 북마크를 삭제했습니다."));
    }


    @PostMapping("/news-like/{newsId}")
    @Operation(summary = "뉴스 좋아요 등록", description = "해당 뉴스가 좋습니다.")
    public ResponseEntity<BaseResponseBody> likeNews (@PathVariable("newsId") Long newsId
    ){
        log.info(">>> [POST] /newsController likeNews - 요청 파라미터: newsId - {}", newsId);
        newsServiceImpl.likeNews(newsId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴스가 좋습니다."));
    }

    @DeleteMapping(value = "news-like/{newsId}/{newsLikeId}")
    @Operation(summary = "뉴스 좋아요 취소", description = "해당 뉴스 좋아요를 취소합니다.")
    public ResponseEntity<BaseResponseBody> deleteNewsLike (
            @PathVariable("newsId") Long newsId,
            @PathVariable("newsLikeId") Long newsLikeId
    ){
        log.info(">>> [DELETE] /newsController deleteBookmark - 요청 파라미터: newsId - {}, newsLikeId - {}", newsId, newsLikeId);
        newsServiceImpl.deleteLike(newsLikeId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 뉴스의 좋아요를 삭제했습니다."));
    }




}
