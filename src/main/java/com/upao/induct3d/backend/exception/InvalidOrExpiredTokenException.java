package com.upao.induct3d.backend.exception;

public class InvalidOrExpiredTokenException extends RuntimeException {
    public InvalidOrExpiredTokenException(String message) {
        super(message);
    }
}
