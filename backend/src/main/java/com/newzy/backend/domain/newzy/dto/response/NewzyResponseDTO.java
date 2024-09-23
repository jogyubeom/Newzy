package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewzyResponseDTO {

    @Schema(description = "뉴지 pk", example = "1")
    private Long newzyId;

    @Schema(description = "뉴지 제목", example = "title")
    private String title;

    @Schema(description = "뉴지 내용", example = "content")
    private String content;

    @Schema(description = "뉴지 카테고리", example = "1")
    private Category category;

    @Schema(description = "뉴지 수정여부", example = "false")
    private Boolean isUpdated;

    @Schema(description = "뉴지 삭제여부", example = "false")
    private Boolean isDeleted;

    @Schema(description = "뉴지 좋아요 수", example = "0")
    private int likeCnt;

    @Schema(description = "뉴지 방문자 수", example = "0")
    private int visitCnt;

    // 유저 정보 추후 추가하기

    public NewzyResponseDTO(Long newzyId, String title, String content, Category category) {
        this.newzyId = newzyId;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}


