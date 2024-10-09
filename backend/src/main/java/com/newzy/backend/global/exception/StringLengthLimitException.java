package com.newzy.backend.global.exception;

import lombok.Getter;

@Getter
public class StringLengthLimitException extends RuntimeException  {
    private final String message;

    public StringLengthLimitException() {
        this.message = "입력된 내용의 글자수가 최대 개수를 초과했습니다.";
    }

    public StringLengthLimitException(String message) {
        this.message = message;
    }
}
