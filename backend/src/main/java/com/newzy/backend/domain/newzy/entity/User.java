package com.newzy.backend.domain.newzy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "info")
    private String info;

    @Column(name = "exp")
    private int exp = 0;

    @Column(name = "economy_score")
    private int economyScore;

    @Column(name = "society_score")
    private int societyScore;

    @Column(name = "international_score")
    private int internationalScore;

    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private  List<Newzy> newzy = new ArrayList<>();

    // 뉴지 댓글
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<NewzyComment> newzyComments = new ArrayList<>();

    // 뉴지 북마크
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Bookmark> newzyBookmarks = new ArrayList<>();

    // 뉴지 좋아요
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Like> newzyLike = new ArrayList<>();

    // 팔로잉 ( to_id )
    @OneToMany(mappedBy = "fromId", fetch = FetchType.LAZY)
    private List<Follow> followings = new ArrayList<>();
    // 팔로워 ( from_id )
    @OneToMany(mappedBy = "toId", fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();


    @Builder
    public User(String nickname, String password, String email, LocalDate birth){
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.birth = birth;
    }
}
