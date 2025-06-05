package com.upao.induct3d.backend.domain.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String code;

    @NotBlank
    private String newPassword;

    public ResetPasswordDTO() {}

    public ResetPasswordDTO(String email, String code, String newPassword) {
        this.email = email;
        this.code = code;
        this.newPassword = newPassword;
    }

    public String getEmail() {return email;}
    public String getCode() {return code;}
    public String getNewPassword() {return newPassword;}

    public void setEmail(String email) {this.email = email;}
    public void setCode(String code) {this.code = code;}
    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}
}

