package com.newzy.backend.domain.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.news.dto.request.NewsListGetRequestDTO;
import com.newzy.backend.domain.news.dto.response.*;
import com.newzy.backend.domain.news.entity.*;
import com.newzy.backend.domain.news.repository.*;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.domain.user.service.UserService;
import com.newzy.backend.global.exception.EntityIsFoundException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.newzy.backend.global.exception.NotValidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private final NewsLikeRepositorySupport newsLikeRepositorySupport;
    private final NewsBookmarkRepositorySupport newsBookmarkRepositorySupport;
    private final DailyQuizHistoryRepository dailyQuizHistoryRepository;


    @Override  // branch : feature/get-news의 NewsServiceImpl 참고
    @Transactional(readOnly = true)
    public Map<String, Object> getNewsList(NewsListGetRequestDTO newsListGetRequestDTO) {
        log.info(">>> getNewsList - dto: {}", newsListGetRequestDTO);

        String todayDate = LocalDate.now().toString();  // 오늘 날짜

        int page = newsListGetRequestDTO.getPage();
        int category = newsListGetRequestDTO.getCategory();
        int sort = newsListGetRequestDTO.getSort();
        String keyword = newsListGetRequestDTO.getKeyword();

        Map<String, Object> map = newsRepositorySupport.findNewsList(page, sort, category, keyword);

        List<NewsListGetResponseDTO> newsListGetResponseDTOs = (List<NewsListGetResponseDTO>) map.get("newsList");

        for (NewsListGetResponseDTO news : newsListGetResponseDTOs) {
            String redisKey = "ranking:news:" + todayDate + ":" + news.getNewsId();  // Redis 키
            String redisHit = redisTemplate.opsForValue().get(redisKey);  // Redis에서 조회수 가져오기
            if (redisHit != null) {
                news.setHit(news.getHit() + Integer.parseInt(redisHit));  // 조회수가 있을 경우 DTO에 설정
            }
        }

        return map;
    }
    @Override
    @Transactional(readOnly = true)
    public List<NewsListGetResponseDTO> getHotNewsList() {
        String yesterdayDate = LocalDate.now().minusDays(1).toString();  // 어제 날짜
        String pattern = "ranking:news:" + yesterdayDate + ":*";  // 어제 날짜의 모든 뉴지 조회수

        Set<String> keys = redisTemplate.keys(pattern);  // 해당 패턴에 맞는 키 가져오기

        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();  // 만약 키가 없으면 빈 리스트 반환
        }

        // Redis에서 각 키에 대한 조회수 정보를 가져옴
        List<String> keyList = new ArrayList<>(keys);
        List<String> values = redisTemplate.opsForValue().multiGet(keyList);

        if (values == null || values.isEmpty()) {
            throw new NotValidRequestException("조회수가 없습니다.");
        }

        // 키와 조회수를 쌍으로 묶어서 리스트로 만듦
        List<Map.Entry<String, Integer>> keyValueList = new ArrayList<>();
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            String value = values.get(i);
            if (value != null) {
                keyValueList.add(new AbstractMap.SimpleEntry<>(key, Integer.parseInt(value)));
            }
        }

        // 조회수(value) 기준으로 내림차순 정렬 후 상위 3개의 키 추출
        List<String> topNewsKeys = keyValueList.stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())  // 조회수를 기준으로 내림차순 정렬
                .limit(3)  // 상위 3개의 키 추출
                .map(entry -> {
                    String[] keyParts = entry.getKey().split(":");
                    if (keyParts.length > 3) {
                        return keyParts[3];  // key에서 newsId 추출
                    } else {
                        log.warn("Invalid key format: " + entry.getKey());
                        return null;  // 잘못된 형식의 키는 무시
                    }
                })
                .filter(Objects::nonNull)  // null 값 필터링
                .collect(Collectors.toList());
        log.info(topNewsKeys.toString());
        // 상위 3개의 newsId에 해당하는 News 객체들을 데이터베이스에서 조회한 후 DTO로 변환
        return topNewsKeys.stream()
                .map(newsId -> newsRepository.findById(Long.parseLong(newsId))
                        .map(NewsListGetResponseDTO::convertToDTO)  // News 객체를 DTO로 변환
                        .orElseThrow(() -> new EntityNotFoundException("해당 뉴스 데이터를 찾을 수 없습니다.")))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCardList(Long userId, int page) {

        return newsCardRepositorySupport.findNewsCardList(userId, page);
    }


    @Override
    public NewsCardListGetResponseDTO getCardInfo(Long userId, Long cardId) {

        return newsCardRepositorySupport.findNewsCardInfo(cardId);
    }


    @Override
    public List<NewsRecommendGetResponseDTO> getRecommendedNewsList(Long userId) {
        List<NewsRecommendGetResponseDTO> recommendedNewsList = new ArrayList<>();

        try {
            // 1. 오늘 날짜 계산
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 2. Redis 키 생성
            String clusterId;
            if (userId == 0) // 비회원은 1번 군집으로 설정
                clusterId = "1";
            else
                clusterId = String.valueOf(userService.getClusterId(userId));
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
        NewsDailyGetResponseDTO dto = new NewsDailyGetResponseDTO();

        LocalDate todayDate = LocalDate.now();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 엔티티가 없습니다."));
        boolean isExisted = dailyQuizHistoryRepository.existsByUserAndDate(user, todayDate);

        if (isExisted)
            dto.setSolved(true);
        else
            dto.setSolved(false);

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

                    // 7. JSON 파싱하여 NewsRecommendGetResponseDTO
                    JsonNode newsJsonNode = objectMapper.readTree(newsValue);
                    dto.setNewsId(newsJsonNode.get("news_id").asLong());
                    dto.setSummary(newsJsonNode.get("summary").asText());
                    dto.setLink(newsJsonNode.get("link").asText());
                    dto.setSummary(newsJsonNode.get("summary").asText());
                    dto.setThumbnail(newsJsonNode.get("thumbnail").asText());

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
    public void saveDailyQuiz(Long userId) {
        LocalDate today = LocalDate.now();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 엔티티가 없습니다."));
        boolean isExisted = dailyQuizHistoryRepository.existsByUserAndDate(user, today);
        if (isExisted)
            throw new EntityIsFoundException("이미 데일리 퀴즈를 풀었습니다.");

        dailyQuizHistoryRepository.save(new DailyQuizHistory(user, today));

        user.setExp(user.getExp() + 20);
        userRepository.save(user);
    }


    @Override
    public NewsDetailGetResponseDTO getNewsDetail(Long userId, Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 뉴스 데이터를 조회할 수 없습니다."));

        // redis 조회수 증가
        String todayDate = LocalDate.now().toString();  // 오늘 날짜
        String redisKey = "ranking:news:" + todayDate + ":" + newsId;  // Redis 키

        Long hit = redisTemplate.opsForValue().increment(redisKey);

        // 키가 새로 생성된 경우에만 만료 시간 설정 (24시간)
        if (hit == 1) {
            redisTemplate.expire(redisKey, Duration.ofDays(2));  // 24시간 만료 설정
        }

        NewsDetailGetResponseDTO newsDetailGetResponseDTO =
                newsRepositorySupport.getNewsDetail(news.getNewsId());

        newsDetailGetResponseDTO.setHit((int) (newsDetailGetResponseDTO.getHit() + hit));

        if (userId != 0) {
            User user = userRepository.findByUserId(userId).orElseThrow(
                    () -> new EntityNotFoundException("일치하는 유저를 찾을 수 없습니다.")
            );
            boolean isLiked = newsLikeRepositorySupport.isLikedByUser(userId, newsId);
            if (isLiked) newsDetailGetResponseDTO.setLiked(true);
            boolean isBookmarked = newsBookmarkRepositorySupport.isBookmarkedByUser(userId, newsId);
            if (isBookmarked) newsDetailGetResponseDTO.setBookmarked(true);
            boolean isCollected = newsCardRepository.existsByUserAndNews(user, news);
            if (isCollected) newsDetailGetResponseDTO.setCollected(true);

        }

        return newsDetailGetResponseDTO;
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

        news.setLikeCnt(news.getLikeCnt() + 1);
        newsRepository.save(news);
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
        news.setLikeCnt(news.getLikeCnt() - 1);
        newsRepository.save(news);
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
        // 경험치 더하기
        user.setExp(user.getExp()+10);
        userRepository.save(user);

        // logic 2. 뉴스 카드 생성, 저장
        NewsCard newsCard = NewsCard.convertToEntity(user, news, requestDTO);
        newsCardRepository.save(newsCard);
    }

}
