package com.newzy.backend.domain.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.response.NewsCardListGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsRecommendGetResponseDTO;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.news.repository.*;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.EntityIsFoundException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위해 사용
    private final NewsCardRepositorySupport newsCardRepositorySupport;


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
        LocalDateTime startOf24HoursAgo = now.minusHours(24); // 현재 시간에서 24시간 이전 시점 계산

        return newsRepositorySupport.findTop3NewsByDayWithHighestHits(startOf24HoursAgo, now);
    }


    @Override
    @Transactional(readOnly = true)
    public List<NewsCardListGetResponseDto> getCardList(Long userId) {

        return newsCardRepositorySupport.findNewsCardList(userId);
    }

    @Override
    public NewsCardListGetResponseDto getCardInfo(Long userId, Long cardId) {

        return newsCardRepositorySupport.findNewsCardInfo(cardId);
    }


    @Override
    public List<NewsRecommendGetResponseDTO> getRecommendedNewsList(Long userId) {
        List<NewsRecommendGetResponseDTO> recommendedNewsList = new ArrayList<>();

        try {
            // 1. 오늘 날짜 계산
//            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 2. Redis 키 생성 (cluster_id는 1로 가정)
            String clusterId = "1";
            String redisKey = String.format(":1:recommended_news:%s:cluster_%s", today, clusterId);

            // 3. Redis에서 키 조회
            String value = redisTemplate.opsForValue().get(redisKey);
            if (value != null) {
                // 4. JSON 파싱하여 뉴스 ID 리스트 추출
                JsonNode jsonNode = objectMapper.readTree(value);
                JsonNode newsIdList = jsonNode.get("list");
                if (newsIdList != null && newsIdList.isArray()) {
                    int count = 0;

                    for (JsonNode newsIdNode : newsIdList) {
                        if (count >= 8) break; // 8개까지만 추출

                        Long newsId = newsIdNode.asLong();

                        // 5. 각 뉴스 ID로 Redis에서 뉴스 정보를 조회
                        String newsKey = String.format(":1:news_info:%s:cluster_%s", newsId, clusterId);
                        String newsValue = redisTemplate.opsForValue().get(newsKey);

                        if (newsValue != null) {
                            // 6. JSON 파싱하여 NewsRecommendGetResponseDTO 생성
                            JsonNode newsJsonNode = objectMapper.readTree(newsValue);
                            NewsRecommendGetResponseDTO dto = NewsRecommendGetResponseDTO.builder()
                                    .newsId(newsJsonNode.get("news_id").asLong())
                                    .link(newsJsonNode.get("link").asText())
                                    .summary(newsJsonNode.get("summary").asText())
                                    .thumbnail(newsJsonNode.get("thumbnail").asText())
                                    .build();

                            // 7. DTO 리스트에 추가
                            recommendedNewsList.add(dto);
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }

        return recommendedNewsList;
    }


    @Override
    public NewsDetailGetResponseDto getNewsDetail(Long NewsId) {    // 조회수 + 1
        News news = newsRepository.findById(NewsId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터를 조회할 수 없습니다."));

        // 조회수 + 1 로직 추가
        news.setHit(news.getHit() + 1);
        newsRepository.save(news);

        // DTO로 변환하여 반환
        return newsRepositorySupport.getNewsDetail(news.getNewsId());
    }


    @Override
    public void bookmark(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));
        boolean isBookmark = newsBookmarkRepository.existsByUserAndNews(user, news);

        if (isBookmark) {
            throw new EntityIsFoundException("이미 북마크가 존재합니다.");
        }

        NewsBookmark newsBookmark = new NewsBookmark();
        newsBookmark.setNews(news);
        newsBookmark.setUser(user);

        newsBookmarkRepository.save(newsBookmark);
    }


    @Override
    public void deleteBookmark(Long NewsId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));
        News news = newsRepository.findById(NewsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));
        boolean isBookmark = newsBookmarkRepository.existsByUserAndNews(user, news);

        if (! isBookmark) {
            throw new EntityNotFoundException("해당하는 북마크를 찾을 수 없습니다.");
        }

        newsBookmarkRepository.deleteByUserAndNews(user, news);
    }


    @Override
    public void likeNews(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));

        Boolean isLike = newsLikeRepository.existsByUserAndNews(user, news);

        if (isLike) {
            throw new EntityIsFoundException("해당하는 좋아요가 이미 존재합니다.");
        }

        NewsLike newsLike = new NewsLike();
        newsLike.setNews(news);
        newsLike.setUser(user);
        newsLikeRepository.save(newsLike);
    }


    @Override
    public void deleteLike(Long userId, Long newsId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("해당하는 뉴스 엔티티를 찾을 수 없습니다."));

        Boolean isLike = newsLikeRepository.existsByUserAndNews(user, news);

        if (! isLike) {
            throw new EntityNotFoundException("해당하는 북마크를 찾을 수 없습니다.");
        }

        newsLikeRepository.deleteByUserAndNews(user, news);
    }


    @Override
    public void collectNewsCard(Long userId, NewsCardRequestDTO requestDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티가 없습니다."));
        News news = newsRepository.findById(requestDTO.getNewsId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 뉴스가 없습니다."));

        Boolean isNewsCard = newsCardRepository.existsByUserAndNews(user, news);

        if (isNewsCard) {
            throw new EntityIsFoundException("이미 획득한 뉴스 카드입니다.");
        }

        int category = news.getCategory();

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
                throw new EntityNotFoundException("잘못된 카테고리 값: " + category);
        }
        userRepository.save(user);

        // logic 2. 뉴스 카드 생성, 저장
        NewsCard newsCard = NewsCard.convertToEntity(user, news, requestDTO);
        newsCardRepository.save(newsCard);
    }

}
