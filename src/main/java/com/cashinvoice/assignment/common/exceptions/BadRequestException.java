package com.cashinvoice.assignment.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private final HttpStatus status;

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // default
    }

    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status; // custom status
    }

    public HttpStatus getStatus() {
        return status;
    }
}