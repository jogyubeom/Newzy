package com.newzy.backend.domain.image.dto;

import lombok.*;
import com.newzy.backend.domain.image.entity.Image;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewzyImageGetResponseDTO {
    private Long newzyImageId;

    private Long newzyId;

    private Image image;

    private int order;
    private boolean isThumbnail;
}
