package com.newzy.scheudling.domain.card.entity;

import com.newzy.scheudling.domain.news.entity.News;
import com.newzy.scheudling.global.model.BaseTimeEntity;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Column(name = "summary")
    private String summary;

    @Column(name = "category")
    private int category;

    @Column(name = "score") // 2: 쉽다, 1: 보통이다, 0: 어렵다
    private int score;

}