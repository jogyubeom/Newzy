package com.newzy.backend.domain.news.service;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.news.repository.NewsBookmarkRepository;
import com.newzy.backend.domain.news.repository.NewsLikeRepository;
import com.newzy.backend.domain.news.repository.NewsRepository;
import com.newzy.backend.domain.news.repository.NewsRepositorySupport;
import com.newzy.backend.domain.newzy.repository.NewsCardRepository;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.newzy.backend.global.exception.IllegalStateException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final UserRepository userRepository;
    private final NewsCardRepository newsCardRepository;

    @Override  // branch : feature/get-news의 NewsServiceImpl 참고
    @Transactional(readOnly = true)
    public List<NewsListGetResponseDto> getNewsList(int page, int category) {
        log.info(">>> getNewsList - page: {}. category: {}", page, category);
        List<NewsListGetResponseDto> newsListGetResponseDtoList = newsRepositorySupport.findNewsList(page, category);

        return newsListGetResponseDtoList;
    }


    @Override
    @Transactional(readOnly = true)
    public List<NewsListGetResponseDto> getHotNewsList() {
        List<News> hotNews = newsRepository.findTop3ByOrderByHitDesc();
        List<NewsListGetResponseDto> hotNewsList = new ArrayList<>();

        for (News news : hotNews) {
            NewsListGetResponseDto dto = NewsListGetResponseDto.convertToDTO(news);
            hotNewsList.add(dto);
        }
        return hotNewsList;
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
    public void deleteBookmark(Long NewsId, Long bookmarkId) {
        News news = newsRepository.findById(NewsId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터가 없습니다."));
        NewsBookmark newsBookmark = newsBookmarkRepository.findById(bookmarkId).orElseThrow(() -> new EntityNotFoundException("일치하는 북마크 데이터가 없습니다."));;

        newsBookmarkRepository.deleteById(bookmarkId);
    }


    @Override
    public void likeNews(Long newsId) {
        if (newsId == null) {
            throw new IllegalStateException("해당 아이디의 뉴스를 찾을 수 없습니다." + newsId);
        }
        NewsLike newsLike = new NewsLike();
        newsLike.setNewsId(newsId);
        newsLikeRepository.save(newsLike);
    }


    @Override
    public void deleteLike(Long NewsId, Long newsLikeId) {
        News news = newsRepository.findById(NewsId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터가 없습니다."));
        NewsLike newsLike = newsLikeRepository.findById(newsLikeId).orElseThrow(() -> new EntityNotFoundException("해당하는 좋아요가 없습니다."));

        newsLikeRepository.delete(newsLike);
    }

    @Override
    public void collectNewsCard(NewsCardRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티가 없습니다."));
        News news = newsRepository.findById(requestDTO.getNewsId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 뉴스가 없습니다."));

        int category = news.getCategory();
        if (user == null) {
            throw new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다.");
        }

        // logic 1. 회원 경제, 사회, 세계 점수 변경
        switch (category) {
            case 0:
                user.setEconomyScore(user.getEconomyScore() + requestDTO.getScore());
                break;
            case 1:
                user.setSocietyScore(user.getSocietyScore() + requestDTO.getScore());
                break;
            case 2:
                user.setInternationalScore(user.getInternationalScore() + requestDTO.getScore());
                break;
            default:
                throw new IllegalArgumentException("잘못된 카테고리 값: " + category);
        }
        userRepository.save(user);

        // logic 2. 뉴스 카드 생성, 저장
        NewsCard newsCard = NewsCard.convertToEntity(requestDTO);
        newsCardRepository.save(newsCard);
    }

}
