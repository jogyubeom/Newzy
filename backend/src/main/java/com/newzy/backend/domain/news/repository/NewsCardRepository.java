package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.NewsCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCardRepository extends JpaRepository<NewsCard, Long> {
}
