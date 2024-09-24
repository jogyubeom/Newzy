package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.repository.NewsRepository;
import com.newzy.backend.domain.news.repository.NewsRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepositorySupport newsRepositorySupport;

    @Override
    public List<NewsListGetResponseDto> getNewsList(int page, int category) {
        log.info(">>> getNewsList - page: {}. category: {}", page, category);
        List<NewsListGetResponseDto> newsListGetResponseDtoList = newsRepositorySupport.findNewsList(page, category);

        return newsListGetResponseDtoList;
    }

    @Override
    public NewsDetailGetResponseDto getNewsDetail(Long NewsId) {
        return null;
    }
}
