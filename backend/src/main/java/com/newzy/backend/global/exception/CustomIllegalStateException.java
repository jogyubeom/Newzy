package com.newzy.backend.global.exception;

import lombok.Getter;

@Getter
public class CustomIllegalStateException extends RuntimeException {
    private final String message;

    public CustomIllegalStateException() {
        this.message = "대상 객체의 상태가 적절하지 않습니다.";
    }

    public CustomIllegalStateException(String message) {  this.message = message;
    }
}
