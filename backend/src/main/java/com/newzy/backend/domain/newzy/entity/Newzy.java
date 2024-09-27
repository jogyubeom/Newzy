package com.newzy.backend.domain.newzy.entity;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy")
@ToString
public class Newzy extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_id", unique = true, nullable = false)
    private Long newzyId;

//    @ManyToOne(targetEntity = User.class)
//    @JoinColumn(name = "user_id", updatable = false)
//    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "category")
    private int category;

    @Column(name = "is_updated")
    private boolean isUpdated = Boolean.FALSE;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "like_cnt")
    private int likeCnt = 0;

    @Column(name = "visit_cnt")
    private int visitCnt = 0;

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static Newzy convertToEntity(NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }

    public static Newzy convertToEntity(Long newzyId , NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setNewzyId(newzyId);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }
}
