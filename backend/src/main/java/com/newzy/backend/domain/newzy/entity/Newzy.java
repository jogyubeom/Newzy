package com.newzy.backend.domain.newzy.entity;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "is_updated")
    private boolean isUpdated = Boolean.FALSE;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "like_cnt")
    private int likeCnt = 0;

    @Column(name = "visit_cnt")
    private int visitCnt = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "newzy")
    private List<Bookmark> newzyBookmarks = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "newzy")
    private List<NewzyComment> newzyComments = new ArrayList<>();


    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
