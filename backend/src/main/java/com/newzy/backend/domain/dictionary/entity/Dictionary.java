package com.newzy.backend.domain.dictionary.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "dictionary-index-v2")
public class Dictionary {

    @Id
    private String id;

    private WordInfo wordinfo;
    private SenseInfo senseinfo;

    // Getters and Setters

    @Getter
    @Setter
    public static class WordInfo {
        private String word;
    }

    @Getter
    @Setter
    public static class SenseInfo {
        private String definition;
    }


}
