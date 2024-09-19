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
@Table(name = "newzy_bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_bookmark_id", unique = true, nullable = false)
    private Long newzyBookMarkId;

    // user가 지워지면, 해당 Bookmark 레코드도 사라질 수 있도록 하기 위해 외래키 제약조건 CASCADE
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
