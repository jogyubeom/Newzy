package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsRecommendGetResponseDTO;

import java.util.List;
import java.util.Map;

public interface NewsService {
    Map<String, Object> getNewsList(int page, int category);
    NewsDetailGetResponseDto getNewsDetail(Long NewsId);

    void bookmark(Long NewsId);
    void deleteBookmark(Long NewsId, Long BookmarkId);

    void likeNews(Long NewsId);
    void deleteLike(Long NewsId, Long NewsLikeId);

    List<NewsListGetResponseDto> getHotNewsList();
    List<NewsRecommendGetResponseDTO> getRecommendedNewsList(Long userId);
    void collectNewsCard(NewsCardRequestDTO newsCardRequestDTO);
}
