package com.glenn.myblog.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duplicated User Email")
public class DuplicatedUserEmailException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이미 가입한 이메일입니다. (Email : %s)";

    public DuplicatedUserEmailException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
