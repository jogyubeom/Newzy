package com.newzy.backend.domain.newzy.dto.response;

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

    private Long newzyId;
    private Long userId;
    private String title;
    private String content;
    private String contentText;
    private int category;
    private int likeCnt;
    private int visitCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NewzyListGetResponseDTO convertToDTO(Newzy newzy) {

        if (newzy == null) {
            throw new IllegalArgumentException("뉴지 엔티티가 없습니다.");
        };
        return NewzyListGetResponseDTO.builder()
                .newzyId(newzy.getNewzyId())
                .userId(newzy.getUser().getUserId())
                .title(newzy.getTitle())
                .content(newzy.getContent())
                .contentText(newzy.getContentText())
                .category(newzy.getCategory())
                .likeCnt(newzy.getLikeCnt())
                .visitCnt(newzy.getHit())
                .createdAt(newzy.getCreatedAt())
                .updatedAt(newzy.getUpdatedAt())
                .build();
    }

}
