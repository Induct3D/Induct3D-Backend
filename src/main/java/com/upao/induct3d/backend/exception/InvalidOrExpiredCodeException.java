package com.upao.induct3d.backend.exception;

public class InvalidOrExpiredCodeException extends RuntimeException {
    public InvalidOrExpiredCodeException(String message) {
        super(message);
    }
}
