package com.newzy.backend.domain.dictionary.repository;

import com.newzy.backend.domain.dictionary.entity.SearchWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchWordRepository extends MongoRepository<SearchWord, String> {
    Page<SearchWord> findByUserId(Long userId, Pageable pageable);
    void deleteByUserIdAndWord(Long userId, String word);
}