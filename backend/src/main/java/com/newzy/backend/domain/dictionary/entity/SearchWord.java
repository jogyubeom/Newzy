package com.newzy.backend.domain.dictionary.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "search_word") // MongoDB 컬렉션 이름 설정
public class SearchWord {
    @Id
    private String searchWordId;

    private Long userId;

    private String word;

    private String definitinon;

    private LocalDateTime createdAt;

    // 생성자
    public SearchWord(Long userId, String word, String definitinon) {
        this.userId = userId;
        this.word = word;
        this.definitinon = definitinon;
        this.createdAt = LocalDateTime.now();
    }
}
