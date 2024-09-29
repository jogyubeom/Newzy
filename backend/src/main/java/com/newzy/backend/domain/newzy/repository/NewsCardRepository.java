package com.newzy.backend.domain.newzy.repository;

import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.newzy.entity.NewzyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCardRepository extends JpaRepository<NewsCard, Long> {
}
