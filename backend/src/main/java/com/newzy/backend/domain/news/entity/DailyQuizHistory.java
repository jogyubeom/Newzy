package com.newzy.backend.domain.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "daily_quiz_history")
public class DailyQuizHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_quiz_history_id", unique = true, nullable = false)
    private Long dailyQuizHistory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    public DailyQuizHistory(User user, LocalDate today) {
        this.user = user;
        this.date = today;
    }
}
