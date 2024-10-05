package com.newzy.backend.domain.vocaTest.service;

import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.domain.vocaTest.dto.request.TestResultRequestDto;
import com.newzy.backend.domain.vocaTest.dto.response.TestWordListResponseDto;
import com.newzy.backend.domain.vocaTest.entity.TestWord;
import com.newzy.backend.domain.vocaTest.repository.VocaTestRepository;
import com.newzy.backend.global.exception.EntityIsFoundException;
import com.newzy.backend.global.exception.NotValidRequestException;
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
    private final UserRepository userRepository;

    @Override
    public void saveUserScore(Long userId, TestResultRequestDto scoreList) {

         User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저 엔티티를 찾지 못했습니다."));

        Map<Integer, Integer> categoryScores = scoreList.getCategoryScores();

        try {
            for (Map.Entry<Integer, Integer> entry : categoryScores.entrySet()) {
                int category = entry.getKey();
                int score = entry.getValue();

                if(category == 0) {
                    user.setEconomyScore(score * 5);
                } else if (category == 1) {
                    user.setSocietyScore(score * 5);
                } else {
                    user.setInternationalScore(score * 5);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("어휘력 테스트 결과 저장 중 오류가 발생하였습니다.");
        }
        // 닉네임 저장
        String nickname = scoreList.getNickname();
        if (userRepository.findUserByNickname(nickname).isPresent()) {
            throw new EntityIsFoundException("이미 존재하는 닉네임입니다.");
        } else {
            user.setNickname(nickname);
        }
        // 생년월일 저장
        user.setBirth(scoreList.getBirth());
        // 자기소개 저장
        if (scoreList.getInfo() != null && !scoreList.getInfo().isEmpty()) {
            user.setInfo(scoreList.getInfo());
        }
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
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
