package com.newzy.backend.domain.newzy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(title = "NEWZY_REQ : 뉴지 요청 DTO")
public class NewzyRequestDTO {

    @NotNull(message = "유저 아이디를 입력해주세요!")
    @Schema(description = "유저ID", example = "1")
    private Long userId;

    @NotNull(message = "제목을 입력해주세요!")
    @Schema(description = "뉴지 제목", example = "title")
    private String title;

    @NotNull(message = "내용을 입력해주세요!")
    @Schema(description = "뉴지 내용", example = "content")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요!")
    @Schema(description = "뉴지 카테고리", example = "category")
    private int category;

}
