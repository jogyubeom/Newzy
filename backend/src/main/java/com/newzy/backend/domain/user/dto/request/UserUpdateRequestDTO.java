package com.newzy.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "USER_UPDATE_REQ : 유저 수정 요청 DTO")
public class UserUpdateRequestDTO {
    @Schema(description = "유저 닉네임", example = "nickname")
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;
    @Schema(description = "유저 이메일", example = "abc@gmail.com")
    private String email;
    @Schema(description = "유저 생년월일", example = "2000/01/01")
    private LocalDate birth;
    @Schema(description = "유저 정보", example = "blabla")
    private String info;
    @Schema(description = "소셜 로그인 타입", example = "kakao")
    private String socialLoginType;
}