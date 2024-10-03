package com.newzy.backend.global.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class EntityIsFoundException extends RuntimeException {

    private final String message;

    public EntityIsFoundException() {
        this.message = "해당되는 엔티티가 이미 존재합니다.";
    }

    public EntityIsFoundException(String message) {
        this.message = message;
    }
}
