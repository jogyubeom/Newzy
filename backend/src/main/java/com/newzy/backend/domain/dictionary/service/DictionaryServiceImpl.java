package com.newzy.backend.domain.dictionary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzy.backend.domain.dictionary.dto.request.SearchWordRequestDTO;
import com.newzy.backend.domain.dictionary.dto.request.VocaListRequestDTO;
import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.VocaListResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDTO;
import com.newzy.backend.domain.dictionary.entity.Dictionary;
import com.newzy.backend.domain.dictionary.entity.SearchWord;
import com.newzy.backend.domain.dictionary.repository.DictionaryRepository;
import com.newzy.backend.domain.dictionary.repository.SearchWordRepository;
import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.repository.NewsRepository;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.newzy.backend.global.exception.NotValidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final RedisTemplate<String, String> redisTemplate;

    private final DictionaryRepository dictionaryRepository;
    private final SearchWordRepository searchWordRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    private final long SEARCH_HISTORY_EXPIRATION_TIME = 432000; // 5 days

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    @Override
    public List<DictionaryResponseDTO> searchByWord(String word) {
        List<DictionaryResponseDTO> dictionaryResponseDTOList = new ArrayList<>();
        List<Dictionary> dictionaryList = dictionaryRepository.findByWordinfoWordContaining(word);
        int cnt = 0;
        for (Dictionary dictionary : dictionaryList) {
            if (cnt == 7) break; // 총 7개만 반환
            dictionaryResponseDTOList.add(new DictionaryResponseDTO(
                    dictionary.getId(),
                    dictionary.getWordinfo().getWord().replace("^", " "),
                    dictionary.getSenseinfo().getDefinition()
            ));
            cnt++;
        }
        return dictionaryResponseDTOList;
    }

    @Override
    public void addVocaList(Long userId, VocaListRequestDTO vocaListRequestDTO) {
        // User와 News의 유효성 체크
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(userId + " 와 일치하는 사용자 엔티티를 찾을 수 없습니다.")
        );

        // SearchWord 엔티티 생성 및 저장
        SearchWord searchWord = new SearchWord(user.getUserId(), vocaListRequestDTO.getWord(), vocaListRequestDTO.getDefinition());
        searchWordRepository.save(searchWord);
        log.info("SearchWord 엔티티가 MongoDB에 저장되었습니다: {}", searchWord);
    }

    @Override
    public List<VocaListResponseDTO> getVocaList(SearchWordRequestDTO searchWordRequestDTO) {
        // PageRequest와 정렬 조건 설정
        Sort sorting = (searchWordRequestDTO.getSort() == 0) ? Sort.by(Sort.Direction.DESC, "createdAt") : Sort.by(Sort.Direction.ASC, "createdAt");
        PageRequest pageRequest = PageRequest.of(searchWordRequestDTO.getPage(), 10, sorting);

        // MongoDB에서 검색 기록 조회
        Page<SearchWord> searchWordsPage = searchWordRepository.findByUserId(searchWordRequestDTO.getUserId(), pageRequest);
        List<SearchWord> searchWords = searchWordsPage.getContent();

        List<VocaListResponseDTO> vocaListResponseDTOList = new ArrayList<>();
        for (SearchWord searchWord : searchWords) {
            VocaListResponseDTO vocaListResponseDTO = new VocaListResponseDTO(searchWord.getWord(), searchWord.getDefinitinon());
            vocaListResponseDTOList.add(vocaListResponseDTO);
        }

        return vocaListResponseDTOList;
    }

    @Transactional
    @Override
    public void deleteSearchWordHistory(Long userId, List<String> wordList) {
        // MongoDB에서 userId와 word가 일치하는 검색어 기록을 삭제

        // 단어 목록을 순회하면서 각 단어별로 삭제 메서드 호출
        for (String word : wordList) {
            if (word == null || word.isEmpty()) {
                throw new NotValidRequestException("삭제할 단어가 유효하지 않습니다: " + word);
            }
            searchWordRepository.deleteByUserIdAndWord(userId, word);
        }
        log.info("userId {}와 wordList: {}에 해당하는 검색 기록이 삭제되었습니다.", userId, wordList);
    }

    @Override
    public List<WordCloudResponseDTO> getWordCloudHistory() {
        List<WordCloudResponseDTO> wordCloudList = new ArrayList<>();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 오늘 날짜로부터 3일 이내의 키를 확인하기 위해 3일 전 날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate threeDaysAgo = today.minusDays(3);

        // Redis에 저장된 모든 키 조회 (패턴으로 word:* 를 사용해 검색 기록만 가져오도록 설정)
        Set<String> keys = redisTemplate.keys("word:*");

        if (keys != null) {
            for (String key : keys) {
                String[] keyParts = key.split(":");
                if (keyParts.length >= 3) {
                    String dateStr = keyParts[1]; // yyyy-MM-dd 형식의 날짜 부분

                    // 날짜 비교하여 3일 이내의 키만 처리
                    LocalDate keyDate = LocalDate.parse(dateStr);
                    if (!keyDate.isBefore(threeDaysAgo) && !keyDate.isAfter(today)) {
                        // 키의 값을 조회하여 WordCloudResponseDto 리스트에 추가
                        String jsonStr = valueOperations.get(key);
                        if (jsonStr != null) {
                            try {
                                WordCloudResponseDTO dto = objectMapper.readValue(jsonStr, WordCloudResponseDTO.class);
                                wordCloudList.add(dto);
                            } catch (JsonProcessingException e) {
                                log.error("Error parsing JSON for key {}: {}", key, e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        return wordCloudList;
    }

    @Override
    public void saveSearchWordHistoryToRedis(Long newsId, String word) {
        String today = LocalDate.now().toString();
        String wordKey = "word:" + today + ":" + word;

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 뉴스에서 카테고리 추출
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new EntityNotFoundException(newsId + " 와 일치하는 뉴스 엔티티를 찾을 수 없습니다.")
        );
        int category = news.getCategory(); // 뉴스 엔티티에서 카테고리 정보를 추출

        try {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(wordKey))) {
                String jsonStr = valueOperations.get(wordKey);
                if (jsonStr != null) {
                    WordCloudResponseDTO dto = objectMapper.readValue(jsonStr, WordCloudResponseDTO.class);
                    int newCount = dto.getValue() + 1;

                    // 업데이트된 검색 횟수를 포함한 JSON 생성
                    dto.setValue(newCount);
                    String updatedJsonStr = objectMapper.writeValueAsString(dto);

                    valueOperations.set(wordKey, updatedJsonStr, SEARCH_HISTORY_EXPIRATION_TIME, TimeUnit.SECONDS);
                }
            } else {
                // 새로운 JSON 객체 생성 및 저장
                WordCloudResponseDTO newDto = new WordCloudResponseDTO(word, 1, category);
                String newJsonStr = objectMapper.writeValueAsString(newDto);

                valueOperations.set(wordKey, newJsonStr, SEARCH_HISTORY_EXPIRATION_TIME, TimeUnit.SECONDS);
            }
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON for word {}: {}", word, e.getMessage());
        }
    }
}
