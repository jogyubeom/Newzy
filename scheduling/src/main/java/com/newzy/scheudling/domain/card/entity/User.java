package com.newzy.scheudling.domain.card.entity;


import com.newzy.scheudling.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
@DynamicUpdate
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private Image image;

    @ManyToOne(targetEntity = Cluster.class)
    @JoinColumn(name = "cluster_id", updatable = false)
    private Cluster cluster;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "info")
    private String info;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

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

    @Column(name = "social_login_type", length = 50)
    private String socialLoginType;

    public void setIsDeleted(boolean b) {
        this.isDeleted = b;
    }
}
