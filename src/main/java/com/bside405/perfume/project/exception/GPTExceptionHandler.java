package com.bside405.perfume.project.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GPTExceptionHandler {

    @ExceptionHandler(GPTClientErrorException.class)
    public ResponseEntity<String> handleGPTClientErrorException(GPTClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
    }

    @ExceptionHandler(GPTServerErrorException.class)
    public ResponseEntity<String> handleGPTServerErrorException(GPTServerErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
    }

    @ExceptionHandler(GPTResourceAccessException.class)
    public ResponseEntity<String> handleGPTResourceAccessException(GPTResourceAccessException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
    }

    @ExceptionHandler(GPTRestClientException.class)
    public ResponseEntity<String> handleGPTRestClientException(GPTRestClientException ex) {
        return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
    }
}
