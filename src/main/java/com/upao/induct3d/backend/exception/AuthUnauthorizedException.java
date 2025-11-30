package com.upao.induct3d.backend.exception;

public class AuthUnauthorizedException extends RuntimeException {
    public AuthUnauthorizedException(String message) {
        super(message);
    }
}
