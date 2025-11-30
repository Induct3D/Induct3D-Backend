package com.upao.induct3d.backend.exception;

public class ForbiddenTourAccessException extends RuntimeException {
    public ForbiddenTourAccessException(String message) {
        super(message);
    }
}
