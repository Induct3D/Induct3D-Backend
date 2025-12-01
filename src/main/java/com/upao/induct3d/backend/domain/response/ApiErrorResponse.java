package com.upao.induct3d.backend.domain.response;

public class ApiErrorResponse {
    private String errorCode;
    private String message;
    private Object details;

    public ApiErrorResponse(String errorCode, String message, Object details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    // getters y setters
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public Object getDetails() { return details; }

    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setMessage(String message) { this.message = message; }
    public void setDetails(Object details) { this.details = details; }
}
