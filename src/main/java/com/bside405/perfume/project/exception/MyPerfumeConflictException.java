package com.bside405.perfume.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MyPerfumeConflictException extends RuntimeException {
    public MyPerfumeConflictException(String message) {
        super(message);
    }
}
