package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;

import java.util.List;

public interface NewsService {
    List<NewsListGetResponseDto> getNewsList(int page, int category);
    NewsDetailGetResponseDto getNewsDetail(Long NewsId);
    void bookmark(Long newsId);
    void deleteBookmark(Long bookmarkId);

    void likeNews(Long newsId);
    void deleteLike(Long newsLikeId);


}
