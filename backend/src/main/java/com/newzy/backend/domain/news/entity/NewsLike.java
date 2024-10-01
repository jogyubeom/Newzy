package com.newzy.backend.domain.news.entity;


import com.newzy.backend.domain.user.entity.User;
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

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    // TODO: 추후 다시 수정 (newsLike entity)
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
