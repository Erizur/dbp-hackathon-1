package com.example.oreo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistUsernameException extends BaseException {
    public ExistUsernameException(String message) {
        super(message);
    }
}
