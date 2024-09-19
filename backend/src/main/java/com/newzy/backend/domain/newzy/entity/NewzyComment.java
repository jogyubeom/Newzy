package com.newzy.backend.domain.newzy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy_comment")
public class NewzyComment {

    // 뉴지 댓글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newzy_comment_id", unique = true, nullable = false)
    private int newzyCommentId;

    // 유저 아이디
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    // 뉴지 아이디
    @ManyToOne(targetEntity = Newzy.class)
    @JoinColumn(name="newzy_id", updatable = false)
    private Newzy newzy;

    // 부모 유저 아이디
    @ManyToOne(targetEntity = NewzyComment.class)
    @JoinColumn(name = "parent_comment_id", updatable = false)
    private NewzyComment parentCommentId;

    @Column(name = "newzy_comment")
    private String newzyComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_updated")
    private boolean isUpdated = false;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
