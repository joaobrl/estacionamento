package com.estacionamento.api.domain.exceptions;

public class GenericException extends RuntimeException {
    private final int statusCode;

    public GenericException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}