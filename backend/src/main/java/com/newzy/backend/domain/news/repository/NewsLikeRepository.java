package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import com.newzy.backend.domain.news.entity.NewsLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
}
