package com.newzy.backend.domain.newzy.dto.response;

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
}
