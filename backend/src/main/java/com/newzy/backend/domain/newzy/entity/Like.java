package com.newzy.backend.domain.newzy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy_like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_like_id", unique = true, nullable = false)
    private Long newzyLikeId;

    @ManyToOne
    @JoinColumn(name = "newzy_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Newzy newzy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column
    private boolean isDeleted = Boolean.FALSE;

}
