package com.getir.exception;

public class ForbiddenException extends IllegalArgumentApplicationException {

    public ForbiddenException(String message, String got, String field) {
        super(message, got, field);
    }

    public ForbiddenException(String message, String got) {
        super(message, got);
    }
    public ForbiddenException(String message) {
        super(message);
    }
}
