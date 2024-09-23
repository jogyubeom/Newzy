package com.newzy.backend.domain.newzy.dto.request;

import com.newzy.backend.domain.newzy.entity.Bookmark;
import com.newzy.backend.domain.newzy.entity.Category;
//import com.newzy.backend.domain.newzy.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Description;
import lombok.*;

@Data
@AllArgsConstructor
@Schema(title = "NEWZY_REQ : 뉴지 요청 DTO")
public class NewzyRequestDTO {

    @NotNull(message = "제목을 입력해주세요!")
    @Schema(description = "뉴지 제목", example = "title")
    private String title;

    @NotNull(message = "내용을 입력해주세요!")
    @Schema(description = "뉴지 내용", example = "content")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요!")
    @Schema(description = "뉴지 카테고리", example = "category")
    private Category category;


    // 유저 정보 추후 추가하기

}
