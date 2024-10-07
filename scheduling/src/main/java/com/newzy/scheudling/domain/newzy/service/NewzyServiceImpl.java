package com.newzy.scheudling.domain.newzy.service;


import com.newzy.scheudling.domain.news.entity.News;
import com.newzy.scheudling.domain.news.repository.NewsRepository;
import com.newzy.scheudling.domain.newzy.entity.Newzy;
import com.newzy.scheudling.domain.newzy.repository.NewzyRepository;
import com.newzy.scheudling.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {
    private final RedisTemplate<String, String> redisTemplate;
    private final NewzyRepository newzyRepository;


    public void processNewzyRanking() {
        String yesterdayDate = LocalDate.now().minusDays(1).toString();  // 어제 날짜
        String redisPattern = "ranking:newzy:" + yesterdayDate + ":*";  // ex: ranking:newzy:어제날짜:* 형식의 키

        // Redis에서 어제 날짜의 모든 조회수 키 가져오기
        Set<String> keys = redisTemplate.keys(redisPattern);

        // Redis에서 조회수 데이터를 가져와서 내림차순으로 정렬
        List<Map.Entry<String, Long>> sortedNewzyData = keys.stream()
                .map(key -> Map.entry(key, redisTemplate.opsForValue().increment(key, 0L)))  // 조회수 가져오기
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))  // 조회수 기준 내림차순 정렬
                .collect(Collectors.toList());

        // DB에 조회수 반영
        for (Map.Entry<String, Long> data : sortedNewzyData) {
            String key = data.getKey();
            Long hit = data.getValue();

            String newzyId = key.split(":")[3];  // Redis 키에서 newsId 추출

            // 뉴스 조회수를 DB에 반영하는 로직 (newsId에 해당하는 조회수를 DB에 업데이트)
            Newzy newzy = newzyRepository.findById(Long.parseLong(newzyId))
                    .orElseThrow(() -> new EntityNotFoundException("해당 뉴지를 찾을 수 없습니다: " + newzyId));
            newzy.setHit(hit.intValue() + newzy.getHit());  // 조회수를 int로 변환하여 설정
            newzyRepository.save(newzy);
        }

        // 상위 N개의 조회수 데이터만 Redis에 남기고 나머지는 삭제
        List<String> topNKeys = sortedNewzyData.stream()
                .limit(3)  // 상위 3개의 키만 남김
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 상위 N개를 제외한 나머지 키 삭제
        keys.stream()
                .filter(key -> !topNKeys.contains(key))
                .forEach(redisTemplate::delete);
    }

}
