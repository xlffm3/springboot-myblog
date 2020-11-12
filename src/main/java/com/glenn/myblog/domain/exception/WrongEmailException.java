package com.glenn.myblog.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No such user.")
public class WrongEmailException extends RuntimeException {
    public static final String ERROR_MESSAGE = "가입하지 않은 이메일입니다.";

    public WrongEmailException() {
        super(ERROR_MESSAGE);
    }
}
