package com.newzy.backend.domain.news.repository;

import com.newzy.backend.domain.news.entity.DailyQuizHistory;
import com.newzy.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyQuizHistoryRepository extends JpaRepository<DailyQuizHistory, Long> {
    boolean existsByUserAndDate(User user, LocalDate date);
}