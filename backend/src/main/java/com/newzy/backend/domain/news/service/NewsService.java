package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.request.NewsListGetRequestDTO;
import com.newzy.backend.domain.news.dto.response.*;

import java.util.List;
import java.util.Map;

public interface NewsService {
    Map<String, Object> getNewsList(NewsListGetRequestDTO newsListGetRequestDTO);
    NewsDetailGetResponseDto getNewsDetail(Long NewsId);

    void bookmark(Long userId, Long NewsId);
    void deleteBookmark(Long userId, Long newsId);

    void likeNews(Long userId, Long NewsId);
    void deleteLike(Long userId, Long NewsId);

    NewsDailyGetResponseDTO getDailyContent(Long userId);

    void collectNewsCard(Long userId, NewsCardRequestDTO newsCardRequestDTO);

    List<NewsCardListGetResponseDto> getCardList(Long userId);
    List<NewsListGetResponseDto> getHotNewsList();
    List<NewsRecommendGetResponseDTO> getRecommendedNewsList(Long userId);

    NewsCardListGetResponseDto getCardInfo(Long userId, Long cardId);
}
