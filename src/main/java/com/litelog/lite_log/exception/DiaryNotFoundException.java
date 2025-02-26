package com.litelog.lite_log.exception;

import org.springframework.http.HttpStatus;

public class DiaryNotFoundException extends CustomException {

    public DiaryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}