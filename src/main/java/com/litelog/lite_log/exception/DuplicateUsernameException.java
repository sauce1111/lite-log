package com.litelog.lite_log.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends CustomException {

    public DuplicateUsernameException(String username) {
        super("Username already exists... request username :" + username, HttpStatus.CONFLICT);
    }
}
