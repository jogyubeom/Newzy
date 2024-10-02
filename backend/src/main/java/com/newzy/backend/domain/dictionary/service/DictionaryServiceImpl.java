package com.newzy.backend.domain.dictionary.service;

import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDto;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDto;
import com.newzy.backend.domain.dictionary.entity.Dictionary;
import com.newzy.backend.domain.dictionary.repository.DictionaryRepository;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.repository.NewsRepository;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final long SEARCH_HISTORY_EXPIRATION_TIME = 432000; // 5 days
    private final NewsRepository newsRepository;

    public List<DictionaryResponseDto> searchByWord(String word) {
        List<DictionaryResponseDto> dictionaryResponseDtoList = new ArrayList<>();
        List<Dictionary> dictionaryList = dictionaryRepository.findByWordinfoWordContaining(word);
        for (Dictionary dictionary : dictionaryList) {
            dictionaryResponseDtoList.add(new DictionaryResponseDto(
                    dictionary.getId(),
                    dictionary.getWordinfo().getWord(),
                    dictionary.getSenseinfo().getDefinition()
            ));
        }

        // 검색한 어휘 Redis 에 저장
        saveSearchWordHistoryToRedis(word);

        return dictionaryResponseDtoList;
    }

    @Override
    public void addSearchWordHistory(Long userId, Long newsId, String word) {
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new EntityNotFoundException(newsId + " 와 일치하는 뉴스 엔티티를 찾을 수 없습니다.")
        );

    }

    @Override
    public List<WordCloudResponseDto> getWordCloudHistory() {
        List<WordCloudResponseDto> wordCloudList = new ArrayList<>();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 오늘 날짜로부터 3일 이내의 키를 확인하기 위해 3일 전 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate threeDaysAgo = today.minusDays(3);

        // Redis에 저장된 모든 키 조회 (패턴으로 word:* 를 사용해 검색 기록만 가져오도록 설정)
        Set<String> keys = redisTemplate.keys("word:*");

        if (keys != null) {
            for (String key : keys) {
                // 키에서 날짜 부분 추출 (형식이 "word:yyyy-MM-dd:word" 이므로 ":"을 기준으로 나눠 날짜를 가져옴)
                String[] keyParts = key.split(":");
                if (keyParts.length >= 3) {
                    String dateStr = keyParts[1]; // yyyy-MM-dd 형식의 날짜 부분

                    // 날짜 비교하여 3일 이내의 키만 처리
                    LocalDate keyDate = LocalDate.parse(dateStr);
                    if (!keyDate.isBefore(threeDaysAgo) && !keyDate.isAfter(today)) {
                        // 키의 값을 조회하여 WordCloudResponseDto 리스트에 추가
                        String word = keyParts[2];
                        String countStr = valueOperations.get(key);
                        if (countStr != null) {
                            int count = Integer.parseInt(countStr);
                            wordCloudList.add(new WordCloudResponseDto(word, count));
                        }
                    }
                }
            }
        }

        return wordCloudList;
    }


    @Override
    public void saveSearchWordHistoryToRedis(String word) {
        // 오늘 날짜를 yyyy-MM-dd 형식으로 가져옵니다.
        String today = LocalDate.now().toString();
        // Redis 키를 날짜와 단어로 구성합니다.
        String wordKey = "word:" + today + ":" + word;

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        try {
            // 해당 키가 이미 존재하는 경우, 검색 횟수를 증가시킵니다.
            if (Boolean.TRUE.equals(redisTemplate.hasKey(wordKey))) {
                String cnt = valueOperations.get(wordKey);
                if (cnt != null) {
                    int newCount = Integer.parseInt(cnt) + 1;
                    valueOperations.set(wordKey, String.valueOf(newCount), SEARCH_HISTORY_EXPIRATION_TIME, TimeUnit.SECONDS);
                }
            } else {
                // 해당 키가 존재하지 않는 경우, 값을 1로 설정하고 만료 시간 지정
                valueOperations.set(wordKey, "1", SEARCH_HISTORY_EXPIRATION_TIME, TimeUnit.SECONDS);
            }
        } catch (NumberFormatException e) {
            log.error("Error parsing count value for key {}: {}", wordKey, e.getMessage());
        }
    }
}
