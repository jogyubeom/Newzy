package com.newzy.backend.domain.dictionary.service;

import com.newzy.backend.domain.dictionary.entity.Dictionary;

import java.util.List;

public interface DictionaryService {
    List<Dictionary> searchByWord(String word);
}
