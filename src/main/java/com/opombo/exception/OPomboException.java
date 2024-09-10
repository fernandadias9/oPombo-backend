package com.opombo.exception;

import org.springframework.http.HttpStatus;

public class OPomboException extends Exception {

    HttpStatus status;

    public OPomboException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }
}
