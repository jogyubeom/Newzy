package com.newzy.backend.domain.newzy.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "newzyLike")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy_like")
public class NewzyLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_like_id", unique = true, nullable = false)
    private Long newzyLikeId;

    @ManyToOne
    @JoinColumn(name = "newzy_id", nullable = false)
    private Newzy newzy;


    // TODO: 추후 다시 수정 (newzyLike entity)
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User user;


}
