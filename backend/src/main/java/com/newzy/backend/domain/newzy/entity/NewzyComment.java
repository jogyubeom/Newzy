package com.newzy.backend.domain.newzy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzy.backend.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newzy_comment")
public class NewzyComment extends BaseTimeEntity {

    // 뉴지 댓글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newzyCommentId;

//    // 유저 아이디
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", updatable = false)
//    private User user;

    // 뉴지 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="newzy_id", updatable = false, nullable = false)
    private Newzy newzy;

    // 부모 유저 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private NewzyComment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<NewzyComment> children = new ArrayList<>();

    // 댓글 내용
    @Column(name = "newzy_comment")
    private String newzyComment;


    @Column(name = "is_updated")
    private
    Boolean isUpdated = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
