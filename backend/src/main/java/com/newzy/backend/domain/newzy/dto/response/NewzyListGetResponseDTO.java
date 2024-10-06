package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.image.entity.Image;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class NewzyListGetResponseDTO {

    private Long userId;
    private String nickname;
    private String email;
    private String profile;

    private Long newzyId;
    private String title;
    private String content;
    private String contentText;
    private int category;
    private int likeCnt;
    private String thumbnail;
    private int hit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NewzyListGetResponseDTO convertToDTO(Newzy newzy) {

        if (newzy == null) {
            throw new IllegalArgumentException("뉴지 엔티티가 없습니다.");
        };
        return NewzyListGetResponseDTO.builder()
                .newzyId(newzy.getNewzyId())
                .userId(newzy.getUser().getUserId())
                .nickname(newzy.getUser().getNickname())
                .email(newzy.getUser().getEmail())
                .profile(newzy.getUser().getImage() != null ? newzy.getUser().getImage().getImageUrl() : "https://example.com/default_profile.png")  // 기본 이미지 URL 설정
                .title(newzy.getTitle())
                .content(newzy.getContent())
                .contentText(newzy.getContentText())
                .category(newzy.getCategory())
                .likeCnt(newzy.getLikeCnt())
                .thumbnail(newzy.getThumbnail())
                .hit(newzy.getHit())
                .createdAt(newzy.getCreatedAt())
                .updatedAt(newzy.getUpdatedAt())
                .build();

    }

}
