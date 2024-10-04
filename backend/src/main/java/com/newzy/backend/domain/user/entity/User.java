package com.newzy.backend.domain.user.entity;

import com.newzy.backend.domain.image.entity.Image;
import com.newzy.backend.domain.user.dto.request.UserInfoRequestDTO;
import com.newzy.backend.global.model.BaseTimeEntity;
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


    @Builder
    public User(String nickname, String password, String email, LocalDate birth, String info) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.info = info;
    }

    public static User convertToEntity(UserInfoRequestDTO dto) {
        User user = new User();
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBirth(dto.getBirth());
        user.setInfo(dto.getInfo());

        return user;
    }

    public static User convertToEntity(Long userId, UserInfoRequestDTO dto) {
        User user = new User();
        user.setUserId(userId);
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBirth(dto.getBirth());
        user.setInfo(dto.getInfo());

        return user;
    }

    public void setIsDeleted(boolean b) {
        this.isDeleted = b;
    }
}
