package com.newzy.scheudling.domain.newzy.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.newzy.scheudling.domain.card.dto.CardCountDTO;
import com.newzy.scheudling.domain.newzy.entity.Newzy;
import com.newzy.scheudling.domain.newzy.repository.NewzyRepository;
import com.newzy.scheudling.domain.newzy.repository.NewzyRepositorySupport;
import com.newzy.scheudling.domain.user.entity.User;
import com.newzy.scheudling.domain.user.repository.UserRepository;
import com.newzy.scheudling.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {
    private final RedisTemplate<String, String> redisTemplate;
    private final NewzyRepository newzyRepository;
    private final NewzyRepositorySupport newzyRepositorySupport;
    private final UserRepository userRepository;

    @Override
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

            String newzyId = key.split(":")[3];  // Redis 키에서 newzyId 추출

            // 뉴스 조회수를 DB에 반영하는 로직 (newsId에 해당하는 조회수를 DB에 업데이트)
            Newzy newzy = newzyRepository.findById(Long.parseLong(newzyId))
                    .orElseThrow(() -> new EntityNotFoundException("해당 뉴지를 찾을 수 없습니다: " + newzyId));
            newzy.setHit(hit.intValue() + newzy.getHit());  // 조회수를 int로 변환하여 설정
            newzyRepository.save(newzy);
        }

        // 상위 N개의 조회수 데이터만 Redis에 남기고 나머지는 삭제
        List<String> topNKeys = sortedNewzyData.stream()
                .limit(6)  // 상위 6개의 키만 남김
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
//
        // 경험치 업데이트
        for(String topNKey : topNKeys) {
            String newzyId = topNKey.split(":")[3];  // Redis 키에서 newzyId 추출

            Newzy newzy = newzyRepository.findById(Long.parseLong(newzyId))
                    .orElseThrow(() -> new EntityNotFoundException("해당 뉴지를 찾을 수 없습니다: " + newzyId));
            newzy.getUser().setExp(newzy.getUser().getExp()+20);
            userRepository.save(newzy.getUser());
        }



        // 상위 N개를 제외한 나머지 키 삭제
        keys.stream()
                .filter(key -> !topNKeys.contains(key))
                .forEach(redisTemplate::delete);
    }

    @Override
    public void calculateNewporter(){
        // 특정 기간 정의 (월-금 : 일주일)
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusWeeks(1), LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59));

        // 해당 기간 동안 좋아요가 가장 많이 달린 상위 3개의 뉴지 찾기
        List<Newzy> topNewzies = newzyRepositorySupport.findTop3ByLikesBetweenDates(startDate, endDate);

        // Redis에서 ranking:newporter 아래 모든 데이터를 삭제
        Set<String> keys = redisTemplate.keys("ranking:newporter:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        // 새롭게 상위 3개의 뉴지 데이터를 Redis에 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        for (int i = 0; i < topNewzies.size(); i++) {
            String key = "ranking:newporter:" + topNewzies.get(i).getUser().getUserId();  // ranking:newporter:1, ranking:newporter:2 형식으로 저장
            int value = topNewzies.get(i).getLikeCnt();
            valueOperations.set(key, String.valueOf(value), 691200, TimeUnit.SECONDS);  // 691200초 = 8일 동안 유지
            log.info("key = " + key + ", value = " + value);
            // 경험치 업데이트
            User user = userRepository.findByUserId(topNewzies.get(i).getUser().getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
            user.setExp(user.getExp()+30);
            userRepository.save(user);
        }
        log.info("뉴포터 랭킹이 성공적으로 Redis에 저장되었습니다.");
    }

}
