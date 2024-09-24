package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Category;
import com.newzy.backend.domain.newzy.entity.Newzy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "NEWZY_RES : 뉴지 응답 DTO")
public class NewzyResponseDTO {

    @Schema(description = "뉴지 pk", example = "1")
    private Long newzyId;

    @Schema(description = "뉴지 제목", example = "title")
    private String title;

    @Schema(description = "뉴지 내용", example = "content")
    private String content;

    @Schema(description = "뉴지 카테고리", example = "1")
    private Category category;

    @Schema(description = "뉴지 좋아요 수", example = "0")
    private int likeCnt;

    @Schema(description = "뉴지 방문자 수", example = "0")
    private int visitCnt;

    // 유저 정보 추후 추가하기

    public static NewzyResponseDTO convertToDTO(Newzy newzy){
        if (newzy == null){ return null; }

        return new NewzyResponseDTO(newzy.getNewzyId(), newzy.getTitle(), newzy.getContent(), newzy.getCategory(), newzy.getLikeCnt(), newzy.getVisitCnt());
    }
}


