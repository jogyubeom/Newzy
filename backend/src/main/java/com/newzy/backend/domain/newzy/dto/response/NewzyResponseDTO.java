package com.newzy.backend.domain.newzy.dto.response;

import com.newzy.backend.domain.newzy.entity.Category;

public class NewzyResponseDTO {

    private Long newzyId;
    private String title;
    private String content;
    private Category category;
    private Boolean isUpdated;
    private Boolean isDeleted;
    private int likeCnt;
    private int visitCnt;

    // 유저 정보 추후 추가하기

    public NewzyResponseDTO(Long newzyId, String title, String content, Category category) {
        this.newzyId = newzyId;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}


