package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Newzy;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class NewzyListGetResponseDTO {

    private Long newzyId;
    private String title;
    private String content;
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
                .title(newzy.getTitle())
                .content(newzy.getContent())
                .category(newzy.getCategory())
                .likeCnt(newzy.getLikeCnt())
                .visitCnt(newzy.getVisitCnt())
                .createdAt(newzy.getCreatedAt())
                .updatedAt(newzy.getUpdatedAt())
                .build();
    }

}
