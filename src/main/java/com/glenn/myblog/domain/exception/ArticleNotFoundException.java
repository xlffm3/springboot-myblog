package com.glenn.myblog.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot Find Article.")
public class ArticleNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "There is no such article. (ID : %d)";

    public ArticleNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
