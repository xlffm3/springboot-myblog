package com.glenn.myblog.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Password.")
public class WrongPasswordException extends RuntimeException {
    public static final String ERROR_MESSAGE = "패스워드가 틀렸습니다.";

    public WrongPasswordException() {
        super(ERROR_MESSAGE);
    }
}
