package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.news.repository.NewsBookmarkRepository;
import com.newzy.backend.domain.news.repository.NewsLikeRepository;
import com.newzy.backend.domain.news.repository.NewsRepository;
import com.newzy.backend.domain.news.repository.NewsRepositorySupport;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.newzy.backend.global.exception.IllegalStateException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsRepositorySupport newsRepositorySupport;
    private final NewsBookmarkRepository newsBookmarkRepository;
    private final NewsLikeRepository newsLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NewsListGetResponseDto> getNewsList(int page, int category) {
        log.info(">>> getNewsList - page: {}. category: {}", page, category);
        List<NewsListGetResponseDto> newsListGetResponseDtoList = newsRepositorySupport.findNewsList(page, category);

        if (newsListGetResponseDtoList.isEmpty()) {
            throw new EntityNotFoundException("일치하는 뉴스 데이터를 조회할 수 없습니다.");
        }

        return newsListGetResponseDtoList;
    }

    @Override
    public NewsDetailGetResponseDto getNewsDetail(Long NewsId) {    // 조회수 + 1
        News news = newsRepository.findById(NewsId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터를 조회할 수 없습니다."));

        // 조회수 + 1 로직 추가
        news.setHit(news.getHit() + 1);
        newsRepository.save(news);

        // DTO로 변환하여 반환
        return NewsDetailGetResponseDto.convertToDTO(news);

    }

    @Override
    public void bookmark(Long newsId) {

        if (newsId == null) {
            throw new IllegalStateException("해당 아이디의 뉴스를 찾을 수 없습니다.: " + newsId);
        }

        NewsBookmark newsBookmark = new NewsBookmark();
        newsBookmark.setNewsId(newsId);
        newsBookmarkRepository.save(newsBookmark);

    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        NewsBookmark newsBookmark = newsBookmarkRepository.findById(bookmarkId).orElseThrow(() -> new EntityNotFoundException("일치하는 북마크 데이터가 없습니다."));;

        newsBookmarkRepository.deleteById(bookmarkId);
    }

    @Override
    public void likeNews(Long newsId) {
        if (newsId == null) {
            throw new IllegalStateException("해당 아이디의 뉴스를 찾을 수 없습니다." + newsId);
        }
        NewsLike newsLike = new NewsLike();
        newsLike.setNewsLikeId(newsId);
        newsLikeRepository.save(newsLike);
    }

    @Override
    public void deleteLike(Long newsLikeId) {
        NewsLike newsLike = newsLikeRepository.findById(newsLikeId).orElseThrow(() -> new java.lang.IllegalStateException("해당하는 좋아요가 없습니다."));

        newsLikeRepository.delete(newsLike);
    }

}
