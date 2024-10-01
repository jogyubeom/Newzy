package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;

import java.util.List;

public interface NewsService {
    List<NewsListGetResponseDto> getNewsList(int page, int category);
    NewsDetailGetResponseDto getNewsDetail(Long NewsId);

    void bookmark(Long NewsId);
    void deleteBookmark(Long NewsId, Long BookmarkId);

    void likeNews(Long NewsId);
    void deleteLike(Long NewsId, Long NewsLikeId);

    List<NewsListGetResponseDto> getHotNewsList();

    void collectNewsCard(NewsCardRequestDTO newsCardRequestDTO);
}
