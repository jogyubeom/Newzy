package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewzyListResponseDto {
    private Long NewzyId;
    private String title;
    private String content;
    private Category category;

}
