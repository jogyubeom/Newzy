package com.newzy.backend.domain.dictionary.service;

import com.newzy.backend.domain.dictionary.entity.Dictionary;
import com.newzy.backend.domain.dictionary.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public List<Dictionary> searchByWord(String word) {
        return dictionaryRepository.findByWordinfoWordContaining(word);
    }
}
