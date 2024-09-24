package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.repository.NewsRepositorySupport;
import com.newzy.backend.global.exception.EntityNotFoundException;
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

        if (newsListGetResponseDtoList.isEmpty()) {
            throw new EntityNotFoundException("일치하는 뉴스 데이터를 조회할 수 없습니다.");
        }

        return newsListGetResponseDtoList;
    }

    @Override
    public NewsDetailGetResponseDto getNewsDetail(Long NewsId) {
        return null;
    }
}
