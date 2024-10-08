package com.newzy.backend.domain.newzy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Schema(title = "NEWZY_IMAGE_RES : 뉴지 이미지 응답 DTO")
public class NewzyImageResponseDTO {
    private String url;
}
