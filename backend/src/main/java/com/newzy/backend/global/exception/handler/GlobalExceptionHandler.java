package com.newzy.backend.global.exception.handler;

import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import com.newzy.backend.global.exception.NotValidRequestException;
import com.newzy.backend.global.exception.model.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("EntityNotFoundException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());
        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomIllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponseDto> handleCustomIllegalStateException(CustomIllegalStateException ex, HttpServletRequest request) {
        log.error("CustomIllegalStateException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());

        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponseDto> handleNotValidRequestException(NotValidRequestException ex, HttpServletRequest request) {
        log.error("NotValidRequestException 발생 - URL: {}, Message: {}", request.getRequestURI(), ex.getMessage());

        ExceptionResponseDto response = ExceptionResponseDto.of(
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}