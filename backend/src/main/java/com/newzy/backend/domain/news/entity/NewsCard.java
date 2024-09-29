package com.newzy.backend.domain.news.entity;

import com.newzy.backend.domain.news.dto.request.NewsCardRequestDTO;
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
    private Long cardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "summary")
    private String summary;

    @Column(name = "category")
    private int category;

    public static NewsCard convertToEntity(NewsCardRequestDTO requestDto){
        if (requestDto == null){
            throw new IllegalStateException("요청 DTO가 없습니다.");
        }
        NewsCard newsCard = new NewsCard();
        newsCard.setUserId(requestDto.getUserId());
        newsCard.setNewsId(requestDto.getNewsId());
        newsCard.setSummary(requestDto.getSummary());
        newsCard.setCategory(newsCard.getCategory());
        return newsCard;
    }

}
