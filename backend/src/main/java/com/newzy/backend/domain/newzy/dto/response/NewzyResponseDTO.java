package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.image.entity.Image;
import com.newzy.backend.domain.newzy.entity.Newzy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(title = "NEWZY_RES : 뉴지 응답 DTO")
public class NewzyResponseDTO {

    @Schema(description = "뉴지 pk", example = "1")
    private Long newzyId;

    @Schema(description = "유저 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "유저 이메일", example = "email")
    private String email;

    @Schema(description = "유저 프로필", example = "img")
    private String profile;

    @Schema(description = "뉴지 제목", example = "title")
    private String title;

    @Schema(description = "뉴지 내용", example = "content")
    private String content;

    @Schema(description = "뉴지 내용 text", example = "content text")
    private String contentText;

    @Schema(description = "뉴지 카테고리", example = "1")
    private int category;

    @Schema(description = "뉴지 좋아요 수", example = "0")
    private int likeCnt;

    @Schema(description = "뉴지 방문자 수", example = "0")
    private int hit;

    @Schema(description = "", example = "https://abcdesdfklj")
    private String thumbnail;

    @Schema(description = "삭제 여부", example = "false")
    private boolean isDeleted;

    @Schema(description = "뉴지 작성시간")
    private LocalDateTime createdAt;

    // 유저 정보 추후 추가하기
    public static NewzyResponseDTO convertToDTO(Newzy newzy) {
        if (newzy == null) {
            throw new IllegalArgumentException("Newzy entity cannot be null");
        }
        return NewzyResponseDTO.builder()
                .newzyId(newzy.getNewzyId())
                .nickname(newzy.getUser().getNickname())
                .profile(newzy.getUser().getImage().getImageUrl())
                .title(newzy.getTitle())
                .content(newzy.getContent())
                .contentText(newzy.getContentText())
                .category(newzy.getCategory())
                .likeCnt(newzy.getLikeCnt())
                .hit(newzy.getHit())
                .thumbnail(newzy.getThumbnail())
                .createdAt(newzy.getCreatedAt())
                .isDeleted(newzy.isDeleted())  // 삭제 여부 추가
                .build();
    }
}


