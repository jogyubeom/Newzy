package com.newzy.backend.global.exception;

import lombok.Getter;

@Getter
public class IllegalStateException extends RuntimeException {
    private final String message;

    public IllegalStateException() {
        this.message = "대상 객체의 상태가 적절하지 않습니다.";
    }

    public IllegalStateException(String message) {  this.message = message;
    }
}
