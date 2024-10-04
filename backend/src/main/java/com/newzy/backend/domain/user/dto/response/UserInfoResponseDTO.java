package com.newzy.backend.domain.user.dto.response;

import com.newzy.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(title = "USER_INFO_RES : 회원 등록 응답 DTO")
public class UserInfoResponseDTO {

    @Schema(description = "회원 pk", example = "1")
    private Long userId;

    @Schema(description = "회원 군집", example = "1")
    private int clusterId;

    @Schema(description = "회원 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "회원 이메일", example = "aaa@gmail.com")
    private String email;

    @Schema(description = "회원 비밀번호", example = "********")
    private String password;

    @Schema(description = "회원 생년월일", example = "2000/01/01")
    private LocalDate birth;

    @Schema(description = "회원 정보", example = "blabla")
    private String info;

    @Schema(description = "회원 경험치", example = "0")
    private int exp;

    @Schema(description = "경제 점수", example = "0")
    private int economyScore;

    @Schema(description = "사회 점수", example = "0")
    private int societyScore;

    @Schema(description = "국제 점수", example = "0")
    private int internationalScore;

    @Schema(description = "상태", example = "0")
    private int state;

    @Schema(description = "소셜로그인 타입", example = "0")
    private String socialLoginType;

    public static UserInfoResponseDTO convertToDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User 엔티티 값이 없습니다.");
        }
        return UserInfoResponseDTO.builder()
                .userId(user.getUserId())
                .clusterId(user.getCluster().getClusterId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .birth(user.getBirth())
                .info(user.getInfo())
                .exp(user.getExp())
                .economyScore(user.getEconomyScore())
                .societyScore(user.getSocietyScore())
                .internationalScore(user.getInternationalScore())
                .state(user.getState())
                .socialLoginType(user.getSocialLoginType())
                .build();
    }

}
