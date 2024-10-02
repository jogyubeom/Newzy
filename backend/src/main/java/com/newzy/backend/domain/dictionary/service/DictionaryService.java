package com.newzy.backend.domain.dictionary.service;

import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDto;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDto;

import java.util.List;

public interface DictionaryService {
    List<DictionaryResponseDto> searchByWord(String word);
    void addSearchWordHistory(Long userId, Long newsId, String word);
    List<WordCloudResponseDto> getWordCloudHistory();
    void saveSearchWordHistoryToRedis(String word);
}
