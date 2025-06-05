package com.upao.induct3d.backend.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResetRequestDTO {
    @NotBlank @Email
    private String email;

    public ResetRequestDTO() {}
    public ResetRequestDTO(String email) {this.email = email;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}

