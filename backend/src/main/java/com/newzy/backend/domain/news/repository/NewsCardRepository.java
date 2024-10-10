package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsCard;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCardRepository extends JpaRepository<NewsCard, Long> {
    Boolean existsByUserAndNews(User user, News news);
}
