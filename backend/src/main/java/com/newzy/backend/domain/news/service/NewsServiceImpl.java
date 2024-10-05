package com.newzy.backend.domain.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.request.NewsListGetRequestDTO;
import com.newzy.backend.domain.news.dto.response.NewsDailyGetResponseDTO;
import com.newzy.backend.domain.news.dto.response.NewsDetailGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsListGetResponseDto;
import com.newzy.backend.domain.news.dto.response.NewsRecommendGetResponseDTO;
import com.newzy.backend.domain.news.dto.request.NewsListGetRequestDTO;
import com.newzy.backend.domain.news.dto.response.*;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.news.repository.*;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.exception.EntityIsFoundException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserService userService;
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
    public Map<String, Object> getNewsList(NewsListGetRequestDTO newsListGetRequestDTO) {
        log.info(">>> getNewsList - dto: {}", newsListGetRequestDTO);

        int page = newsListGetRequestDTO.getPage();
        int category = newsListGetRequestDTO.getCategory();
        int sort = newsListGetRequestDTO.getSort();
        String keyword = newsListGetRequestDTO.getKeyword();

        return newsRepositorySupport.findNewsList(page, sort, category, keyword);
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
    public Map<String, Object> getCardList(Long userId, int page) {

        return newsCardRepositorySupport.findNewsCardList(userId, page);
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
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 2. Redis 키 생성
            String clusterId = String.valueOf(userService.getClusterId(userId));
            String redisKey = String.format(":1:recommended_news:%s:cluster_%s", today, clusterId);

            // 3. Redis에서 키 조회
            String value = redisTemplate.opsForValue().get(redisKey);
            if (value != null) {
                // 4. JSON 파싱하여 뉴스 ID 리스트 추출
                JsonNode jsonNode = objectMapper.readTree(value);
                JsonNode newsIdList = jsonNode.get("list");
                if (newsIdList != null && newsIdList.isArray()) {
                    int count = 0;
                    int idx = 0;
                    for (JsonNode newsIdNode : newsIdList) {
                        if (idx == 0) { // 첫번째 기사는 데일리 기사로 선정
                            idx++;
                            continue;
                        }
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
    public NewsDailyGetResponseDTO getDailyContent(Long userId) {
        NewsDailyGetResponseDTO dto = null;

        try {
            // 1. 오늘 날짜 계산
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 2. Redis 키 생성
            String clusterId = String.valueOf(userService.getClusterId(userId));
            String redisKey = String.format(":1:recommended_news:%s:cluster_%s", today, clusterId);
            // 3. Redis에서 키 조회
            String value = redisTemplate.opsForValue().get(redisKey);

            if (value == null) {
                throw new EntityNotFoundException("데일리 기사 목록을 조회할 수 없습니다.");
            }

            // 4. JSON 파싱하여 뉴스 ID 리스트 추출
            JsonNode jsonNode = objectMapper.readTree(value);
            JsonNode newsIdList = jsonNode.get("list");

            // 5. 뉴스 리스트에서 첫번째 뉴스부터 최대 5번까지 시도하여 조회
            for (int i = 0; i < 5; i++) {
                try {
                    JsonNode newsIdNode = newsIdList.get(i);
                    Long newsId = newsIdNode.asLong();

                    // 6. 뉴스 ID로 Redis에서 뉴스 정보를 조회
                    String newsKey = String.format(":1:news_info:%s:cluster_%s", newsId, clusterId);
                    String newsValue = redisTemplate.opsForValue().get(newsKey);

                    if (newsValue == null) {
                        log.error("key: {} 의 value를 조회하지 못했습니다.", newsKey);
                        continue; // 뉴스 정보를 찾지 못하면 다음 인덱스로 넘어감
                    }

                    // 7. JSON 파싱하여 NewsRecommendGetResponseDTO 생성
                    JsonNode newsJsonNode = objectMapper.readTree(newsValue);
                    dto = NewsDailyGetResponseDTO.builder()
                            .newsId(newsJsonNode.get("news_id").asLong())
                            .link(newsJsonNode.get("link").asText())
                            .summary(newsJsonNode.get("summary").asText())
                            .thumbnail(newsJsonNode.get("thumbnail").asText())
                            .build();

                    // 8. 퀴즈 정보 조회
                    String redisQuizKey = String.format(":1:quiz:%s", dto.getNewsId());
                    String quizValue = redisTemplate.opsForValue().get(redisQuizKey);

                    if (quizValue == null) {
                        log.error("key: {} 의 value를 조회하지 못했습니다.", redisQuizKey);
                        continue;
                    }

                    // 9. JSON 파싱하여 퀴즈 정보를 DTO에 설정
                    JsonNode quizJsonNode = objectMapper.readTree(quizValue);
                    dto.setQuestion(quizJsonNode.get("question").asText());
                    dto.setOption1(quizJsonNode.get("option1").asText());
                    dto.setOption2(quizJsonNode.get("option2").asText());
                    dto.setOption3(quizJsonNode.get("option3").asText());
                    dto.setOption4(quizJsonNode.get("option4").asText());
                    dto.setAnswer(quizJsonNode.get("answer").asText());

                    return dto; // 성공적으로 DTO가 만들어졌을 때 반환
                } catch (Exception e) {
                    log.warn("뉴스 정보 조회 중 예외 발생 - 인덱스 {}: {}", i, e.getMessage());
                    // 예외가 발생하면 다음 인덱스로 넘어가서 다시 시도
                }
            }
            // 5개의 인덱스에 대해 모두 실패한 경우
            throw new EntityNotFoundException("데일리 뉴스를 조회할 수 없습니다.");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("데일리 컨텐츠 조회 중 오류가 발생했습니다.", e);
        }
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

        if (!isBookmark) {
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

        if (!isLike) {
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
