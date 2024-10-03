package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsLike;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
    Boolean existsByUserAndNews(User user, News news);

    void deleteByUserAndNews(User user, News news);
}
