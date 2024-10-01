package com.newzy.backend.domain.news.entity;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "news_card")
public class NewsCard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_card_id")
    private Long newsCardId;

    // TODO: 1. user 정보 추가 후 다시 수정
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Column(name = "summary")
    private String summary;

    @Column(name = "category")
    private int category;

    @Column(name = "score") // 2: 쉽다, 1: 보통이다, 0: 어렵다
    private int score;

    public static NewsCard convertToEntity(News news, NewsCardRequestDTO requestDto){
        if (requestDto == null){
            throw new IllegalStateException("요청 DTO가 없습니다.");
        }
        NewsCard newsCard = new NewsCard();
        // TODO: 2. user 정보 추가 후 다시 수정
//        newsCard.setUserId(requestDto.getUserId());
        newsCard.setNews(news);
        newsCard.setScore(requestDto.getScore());
        newsCard.setSummary(requestDto.getSummary());
        newsCard.setCategory(newsCard.getCategory());
        return newsCard;
    }

}
