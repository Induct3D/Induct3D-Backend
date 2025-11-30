package com.upao.induct3d.backend.exception;

public class ToursNotFoundException extends RuntimeException {
    public ToursNotFoundException(String message) {
        super(message);
    }
}
