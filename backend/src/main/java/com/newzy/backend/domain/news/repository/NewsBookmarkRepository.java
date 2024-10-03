package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsBookmarkRepository extends JpaRepository<NewsBookmark, Long> {

    boolean existsByUserAndNews(User user, News news);

    void deleteByUserAndNews(User user, News news);
}
