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
import com.newzy.backend.domain.news.repository.NewsCardRepository;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getNewsList(int page, int category) {
        log.info(">>> getNewsList - page: {}. category: {}", page, category);

        return newsRepositorySupport.findNewsList(page, category);
    }


    @Override
    @Transactional(readOnly = true)
    public List<NewsListGetResponseDto> getHotNewsList() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);

        return newsRepositorySupport.findTop3NewsByDayWithHighestHits(startOfDay);
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
        // TODO: 유저 토큰, userId 받아오는 로직 추후 추가
        // TODO: 유저 ID , 뉴스 ID로 조회해서 중북 북마크 예외처리 로직 추후 추가
        NewsBookmark newsBookmark = new NewsBookmark();
        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));
        newsBookmark.setNews(news);
        // newsBookmark.setUser(user);

        newsBookmarkRepository.save(newsBookmark);
    }


    @Override
    public void deleteBookmark(Long NewsId, Long bookmarkId) {

        // TODO: 중복 삭제 로직 추후 추가
        News news = newsRepository.findById(NewsId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터가 없습니다."));
        NewsBookmark newsBookmark = newsBookmarkRepository.findById(bookmarkId).orElseThrow(() -> new EntityNotFoundException("일치하는 북마크 데이터가 없습니다."));;

        newsBookmarkRepository.deleteById(bookmarkId);
    }


    @Override
    public void likeNews(Long newsId) {
        // TODO: 유저 토큰, userId 받아오는 로직 추후 추가
        // TODO: 유저 ID , 뉴스 ID로 조회해서 중북 북마크 예외처리 로직 추후 추가
        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));
        NewsLike newsLike = new NewsLike();
        newsLike.setNews(news);
        newsLikeRepository.save(newsLike);
    }


    @Override
    public void deleteLike(Long NewsId, Long newsLikeId) {
        // TODO: 중복 삭제 로직 추후 추가
        News news = newsRepository.findById(NewsId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터가 없습니다."));
        NewsLike newsLike = newsLikeRepository.findById(newsLikeId).orElseThrow(() -> new EntityNotFoundException("해당하는 좋아요가 없습니다."));

        newsLikeRepository.delete(newsLike);
    }

    @Override
    public void collectNewsCard(NewsCardRequestDTO requestDTO) {
//        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티가 없습니다."));
        News news = newsRepository.findById(requestDTO.getNewsId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 뉴스가 없습니다."));

        int category = news.getCategory();
        // TODO : 획득한 뉴스카드를 중복 획득하지 않는 로직 추가

        // TODO : 추후 로그인 기능 추가되면 다시 수정
//        // logic 1. 회원 경제, 사회, 세계 점수 변경
//        switch (category) {
//            case 0:
//                user.setEconomyScore(user.getEconomyScore() + requestDTO.getScore());
//                break;
//            case 1:
//                user.setSocietyScore(user.getSocietyScore() + requestDTO.getScore());
//                break;
//            case 2:
//                user.setInternationalScore(user.getInternationalScore() + requestDTO.getScore());
//                break;
//            default:
//                throw new EntityNotFoundException("잘못된 카테고리 값: " + category);
//        }
//        userRepository.save(user);

        // logic 2. 뉴스 카드 생성, 저장
        NewsCard newsCard = NewsCard.convertToEntity(news, requestDTO);
        newsCardRepository.save(newsCard);
    }

}
