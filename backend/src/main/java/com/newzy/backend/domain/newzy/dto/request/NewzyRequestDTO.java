package com.newzy.backend.domain.newzy.dto.request;

import com.newzy.backend.domain.newzy.entity.Bookmark;
import com.newzy.backend.domain.newzy.entity.Category;
import com.newzy.backend.domain.newzy.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class NewzyRequestDTO {

    @NotNull(message = "제목을 입력해주세요!")
    private String title;

    @NotNull(message = "내용을 입력해주세요!")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요!")
    private Category category;

    // 유저 정보 추후 추가하기

}
