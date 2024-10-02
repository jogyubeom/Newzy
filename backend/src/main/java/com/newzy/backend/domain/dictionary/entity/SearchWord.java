package com.newzy.backend.domain.dictionary.entity;

import com.newzy.backend.domain.news.entity.News;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class SearchWord {
    @Id
    private String searchWordId;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;
}
