package com.bside405.perfume.project.exception;

import org.springframework.http.HttpStatusCode;

public class GPTClientErrorException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public GPTClientErrorException(String message, Throwable cause, HttpStatusCode statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}