package com.upao.induct3d.backend.exception;

public class TourNotFoundException extends RuntimeException {
    public TourNotFoundException(String message) {
        super(message);
    }
}