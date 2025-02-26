package com.litelog.lite_log.exception;

import org.springframework.http.HttpStatus;

public class FileUploadException extends CustomException {

    public FileUploadException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
