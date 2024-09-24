//package com.newzy.backend.domain.newzy.entity;
//
//import com.newzy.backend.global.model.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.DynamicUpdate;
//
//import java.time.LocalDate;
//
//@Entity
//@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "user")
//@DynamicUpdate
//public class User extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id", unique = true, nullable = false)
//    private Long userId;
//
//    @Column(name = "nickname", nullable = false, length = 50)
//    private String nickname;
//
//    @Column(name = "email", unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @Column(name = "birth", nullable = false)
//    private LocalDate birth;
//
//    @Column(name = "info")
//    private String info;
//
//    @Column(name = "exp")
//    private int exp = 0;
//
//    //
//
//    @Column(name = "economy_score")
//    private int economyScore;
//
//    @Column(name = "society_score")
//    private int societyScore;
//
//    @Column(name = "international_score")
//    private int internationalScore;
//
//    @Column(name = "state", nullable = false)
//    private int state;
//
//    @Builder
//    public User(String nickname, String password, String email, LocalDate birth){
//        this.nickname = nickname;
//        this.password = password;
//        this.email = email;
//        this.birth = birth;
//    }
//}
