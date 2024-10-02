package com.newzy.backend.domain.vocaTest.service;

import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDto;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDto;
import com.newzy.backend.domain.vocaTest.entity.TestWord;
import com.newzy.backend.domain.vocaTest.repository.VocaTestRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VocaTestServiceImpl implements VocaTestService {

    private final VocaTestRepository vocaTestRepository;

    @Override
    public void saveUserScore(TestResultRequestDto scoreList) {
        //TODO: requestHeader 로 유저 토큰에서 유저 아이디 뽑아내는 로직 추가
        // 유저 id 조회 후
        // User user = userRepository.findOneById(userId);

        Map<Integer, Integer> categoryScores = scoreList.getCategoryScores();

        try {
            for (Map.Entry<Integer, Integer> entry : categoryScores.entrySet()) {
                int category = entry.getKey();
                int score = entry.getValue();
                /*
                if(category == 0)
                    user.setEconomicScore(score);
                if else (category == 1)
                    user.setSocietyScore(score);
                else
                    user.setInternationalScore(score);
                 */

            }
        } catch (Exception e) {
            log.error("점수를 저장하지 못했습니다", e.getMessage());
            throw new RuntimeException("점수 저장 실패", e);
        }
    }

    @Override
    public List<TestWordListResponseDto> getWordList() {
        List<TestWordListResponseDto> responseList = Collections.synchronizedList(new ArrayList<>());

        for (int category = 0; category <= 2; category++) {
            List<TestWord> words = vocaTestRepository.findByCategory(category);

            if (words.isEmpty()) {
                continue;
            }

            Collections.shuffle(words);     //shuffle 랜덤으로 단어 섞기 
            List<TestWord> randomWords = words.stream()
                    .limit(20)
                    .collect(Collectors.toList());

            randomWords.forEach(word -> responseList.add(TestWordListResponseDto.convertToDto(word)));
        }

        return responseList;
    }
}
