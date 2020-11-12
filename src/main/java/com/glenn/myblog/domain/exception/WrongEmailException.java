package com.glenn.myblog.domain.exception;

public class WrongEmailException extends RuntimeException {
    private static final String ERROR_MESSAGE = "가입하지 않은 이메일입니다.";

    public WrongEmailException() {
        super(ERROR_MESSAGE);
    }
}
