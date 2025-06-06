package com.upao.induct3d.backend.domain;

import org.springframework.http.HttpStatus;

public class MessageDTO {

    private HttpStatus status;
    private String message;

    public MessageDTO() {}

    public MessageDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {return status;}
    public String getMessage() {return message;}

    public void setStatus(HttpStatus status) {this.status = status;}
    public void setMessage(String message) {this.message = message;}
}
