package com.newzy.scheudling.global.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final String message;

    public EntityNotFoundException() {
        this.message = "일치하는 엔티티를 조회할 수 없습니다.";
    }

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}
