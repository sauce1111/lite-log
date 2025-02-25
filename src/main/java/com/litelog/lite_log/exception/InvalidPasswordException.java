package com.litelog.lite_log.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException() {
        super("Passwords do not match.", HttpStatus.UNAUTHORIZED);
    }
}
