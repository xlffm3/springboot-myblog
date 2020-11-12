package com.glenn.myblog.domain.exception;

public class WrongPasswordException extends RuntimeException {
    private static final String ERROR_MESSAGE = "패스워드가 틀렸습니다.";

    public WrongPasswordException() {
        super(ERROR_MESSAGE);
    }
}
