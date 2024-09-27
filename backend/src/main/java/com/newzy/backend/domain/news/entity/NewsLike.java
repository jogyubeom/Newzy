package com.newzy.backend.domain.news.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news_like")
public class NewsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_like_id", unique = true, nullable = false)
    private Long newsLikeId;

    @Column(name = "news_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Long newsId;

}
