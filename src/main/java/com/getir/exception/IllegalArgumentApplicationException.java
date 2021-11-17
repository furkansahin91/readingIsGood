package com.getir.exception;

import lombok.Getter;

@Getter
public class IllegalArgumentApplicationException extends IllegalArgumentException {
    private String got;
    private String field;
    public IllegalArgumentApplicationException(String message, String got, String field) {
        super(message);
        this.got = got;
        this.field = field;
    }
    public IllegalArgumentApplicationException(String message, String got) {
        super(message);
        this.got = got;
    }

    public IllegalArgumentApplicationException(String message) {
        super(message);
    }
}
