package com.litelog.lite_log.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponseDto<T> {

    private final LocalDateTime localDateTime;
    private final int status;
    private final String message;
    private final T data;

    public ApiResponseDto(int status, String message, T data) {
        this.localDateTime = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDto(int status, String message) {
        this(status, message, null);
    }
}
