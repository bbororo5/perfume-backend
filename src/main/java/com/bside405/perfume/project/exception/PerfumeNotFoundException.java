package com.bside405.perfume.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PerfumeNotFoundException extends RuntimeException {
    public PerfumeNotFoundException(String message) {
        super(message);
    }
}
