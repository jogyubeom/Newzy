package com.newzy.scheudling.domain.newzy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NewzyRequestDTO {

    @NotNull(message = "제목을 입력해주세요!")
    private String title;

    @NotNull(message = "내용을 입력해주세요!")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요!")
    private int category;

    private MultipartFile[] images;

}
