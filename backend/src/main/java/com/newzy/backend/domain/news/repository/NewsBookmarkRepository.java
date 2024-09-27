package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsBookmarkRepository extends JpaRepository<NewsBookmark, Long> {
}
