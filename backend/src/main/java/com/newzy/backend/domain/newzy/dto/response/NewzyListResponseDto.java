package com.newzy.backend.domain.newzy.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewzyListResponseDto {
    private Long newzyId;
    private String title;
    private String content;
    private Integer category;
    private LocalDateTime createdAt;
    private Integer likeCnt;
    private Integer visitCnt;

}
