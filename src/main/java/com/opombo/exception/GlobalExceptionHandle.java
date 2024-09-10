package com.opombo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(OPomboException.class)
    public ResponseEntity<String> handleOPomboException(OPomboException e) {
        return new ResponseEntity<>(e.getMessage(), e.status);
    }
}
