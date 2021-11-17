package com.getir.exception;

public class NotFoundException extends IllegalArgumentApplicationException {
    public NotFoundException(String message, String got, String field) {
        super(message, got, field);
    }
}
