package com.newzy.backend.domain.dictionary.service;

import com.newzy.backend.domain.dictionary.dto.request.SearchWordRequestDTO;
import com.newzy.backend.domain.dictionary.dto.response.DictionaryResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.VocaListResponseDTO;
import com.newzy.backend.domain.dictionary.dto.response.WordCloudResponseDTO;

import java.util.List;

public interface DictionaryService {
    List<DictionaryResponseDTO> searchByWord(String word);

    void addSearchWordHistory(Long userId, Long newsId, String word);
    List<VocaListResponseDTO> getSearchWordHistory(SearchWordRequestDTO searchWordRequestDTO);
    void deleteSearchWordHistory(Long userId, String word);
    List<WordCloudResponseDTO> getWordCloudHistory();

    void saveSearchWordHistoryToRedis(Long newsId, String word);

}
