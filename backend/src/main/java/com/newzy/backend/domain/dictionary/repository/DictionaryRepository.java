package com.newzy.backend.domain.dictionary.repository;

import com.newzy.backend.domain.dictionary.entity.Dictionary;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DictionaryRepository extends ElasticsearchRepository<Dictionary, String> {
    @Query("{\"match\": {\"wordinfo.word\": \"?0\"}}")
    List<Dictionary> findByWordinfoWordContaining(String word);
}
