package com.newzy.backend.domain.dictionary.repository;

import com.newzy.backend.domain.dictionary.entity.Dictionary;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface DictionaryRepository extends ElasticsearchRepository<Dictionary, String> {
    @Query("{ \"match\": { \"wordinfo.word\": { \"query\": \"?0\", \"fuzziness\": \"AUTO\" } } }")
    List<Dictionary> findByWordinfoWordContaining(String word);
}