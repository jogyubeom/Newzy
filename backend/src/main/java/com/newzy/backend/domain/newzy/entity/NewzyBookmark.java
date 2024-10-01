package com.newzy.backend.domain.newzy.entity;

import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "newzyBookmark")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy_bookmark")
public class NewzyBookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_bookmark_id", unique = true, nullable = false)
    private Long newzyBookMarkId;

    @ManyToOne
    @JoinColumn(name = "newzy_id", nullable = false)
    private Newzy newzy;

    // TODO: 추후 다시 수정 (newzyBookmark entity)
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User user;

    // hard delete

}
