package com.newzy.scheudling.global.exception.model;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponseDTO {
    private String httpMethod;
    private String requestURL;
    private int httpStatus;
    private String message;
    private LocalDateTime timestamp;

    public ExceptionResponseDTO(String httpMethod, String requestURL, int httpStatus, String message, LocalDateTime timestamp) {
        this.httpMethod = httpMethod;
        this.requestURL = requestURL;
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ExceptionResponseDTO of(String httpMethod, String requestURL, int httpStatus, String message) {
        return new ExceptionResponseDTO(httpMethod, requestURL, httpStatus, message, LocalDateTime.now());
    }
}
