package com.upao.induct3d.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reset_tokens")
public class ResetPassword {

    @Id
    private String id;
    private String email;
    private String code;
    private LocalDateTime expiration;

    public ResetPassword() {}

    public ResetPassword(String email, String code, LocalDateTime expiration) {
        this.email = email;
        this.code = code;
        this.expiration = expiration;
    }

    public String getId() {return id;}
    public String getEmail() {return email;}
    public String getCode() {return code;}
    public LocalDateTime getExpiration() {return expiration;}

    public void setId(String id) {this.id = id;}
    public void setEmail(String email) {this.email = email;}
    public void setCode(String code) {this.code = code;}
    public void setExpiration(LocalDateTime expiration) {this.expiration = expiration;}
}

