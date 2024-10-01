package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findTop3ByCreatedAtAfterOrderByHitDesc(LocalDateTime startOfDay);

}

