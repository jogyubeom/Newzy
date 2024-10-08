package com.newzy.backend.domain.newzy.entity;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.global.exception.StringLengthLimitException;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", length = 3000)
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

    // save newzy
    public static Newzy convertToEntity(User user, NewzyRequestDTO dto){

        if (dto.getTitle().length() > 100) {
            throw new StringLengthLimitException("제목은 최대 100자까지 입력할 수 있습니다.");
        }
        if (dto.getContent().length() > 3000) {
            throw new StringLengthLimitException("본문은 최대 3000자까지 입력할 수 있습니다.");
        }

        Newzy newzy = new Newzy();
        newzy.setUser(user);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setCategory(dto.getCategory());

        return newzy;
    }

    // update newzy
    public static Newzy convertToEntity(User user, Long newzyId , NewzyRequestDTO dto, String contentText){

        if (dto.getTitle().length() > 100) {
            throw new StringLengthLimitException("제목은 최대 100자까지 입력할 수 있습니다.");
        }
        if (dto.getContent().length() > 3000) {
            throw new StringLengthLimitException("본문은 최대 3000자까지 입력할 수 있습니다.");
        }

        Newzy newzy = new Newzy();
        newzy.setUser(user);
        newzy.setNewzyId(newzyId);
        newzy.setTitle(dto.getTitle());
        newzy.setContent(dto.getContent());
        newzy.setContentText(contentText);
        newzy.setCategory(dto.getCategory());

        return newzy;
    }
}
