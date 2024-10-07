package com.newzy.scheudling.domain.card.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzy.scheudling.domain.card.dto.CardCountDTO;
import com.newzy.scheudling.domain.card.repository.NewsCardRepositorySupport;
import com.newzy.scheudling.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsCardServiceImpl implements NewsCardService {
    private final RedisTemplate<String, String> redisTemplate;
    private final NewsCardRepositorySupport newsCardRepositorySupport;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    @Override
    public void calculateBestCardCollector() {
        // 특정 기간 정의 (월-금 : 일주일)
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59));

        // 해당 기간 동안 카드를 가장 많이 수집한 유저 찾기
        CardCountDTO bestCollectorUser = newsCardRepositorySupport.findTopCollectorUserId(startDate, endDate)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 유저가 없습니다."));

        log.info("{} - {} 카드왕 : {}", startDate, endDate, bestCollectorUser);

        // 레디스에 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        try {
            String key = "ranking:card-collector";
            String value = objectMapper.writeValueAsString(bestCollectorUser);
            valueOperations.set(key, value, 691200, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("레디스 저장 중에 에러 발생", e);
        }
    }
}
