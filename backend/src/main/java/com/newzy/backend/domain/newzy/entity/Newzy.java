package com.newzy.backend.domain.newzy.entity;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.user.entity.User;
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
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})

public class Newzy extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_id", unique = true, nullable = false)
    private Long newzyId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_text", columnDefinition = "TEXT", nullable = false)
    private String contentText;

    @Column(name = "category")
    private int category;

    @Column(name = "is_updated")
    private boolean isUpdated = Boolean.FALSE;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "like_cnt")
    private int likeCnt = 0;

    @Column(name = "hit")
    private int hit = 0;

    @Column(name = "thumbnail", length = 255, nullable = true)
    private String thumbnail;


    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static Newzy convertToEntity(User user, NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setUser(user);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }

    public static Newzy convertToEntity(User user, Long newzyId , NewzyRequestDTO dto){
        Newzy newzy = new Newzy();
        newzy.setUser(user);
        newzy.setNewzyId(newzyId);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }
}
