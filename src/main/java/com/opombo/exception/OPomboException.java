package com.opombo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OPomboException extends Exception {

    HttpStatus status;

    public OPomboException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }
}
