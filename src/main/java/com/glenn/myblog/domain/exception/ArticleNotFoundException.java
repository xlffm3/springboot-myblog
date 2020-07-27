package com.glenn.myblog.domain.exception;

public class ArticleNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "There is no such article. (ID : %d)";

    public ArticleNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
