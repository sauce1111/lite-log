package com.litelog.lite_log.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponseDto<T> {

    private final LocalDateTime localDateTime;
    private final HttpStatus status;
    private final String message;
    private final T data;

    public ApiResponseDto(HttpStatus status, String message, T data) {
        this.localDateTime = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDto(HttpStatus status, String message) {
        this(status, message, null);
    }
}
