package com.glenn.myblog.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Duplicated User Email")
public class DuplicatedUserEmailException extends RuntimeException {
    private static final String ERROR_MESSAGE = "There is already same email. (Email : %s)";

    public DuplicatedUserEmailException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
