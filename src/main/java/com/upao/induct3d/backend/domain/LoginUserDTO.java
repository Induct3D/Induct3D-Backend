package com.upao.induct3d.backend.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {

    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "username is mandatory")
    private String password;

    public String getUsername() {return username;}
    public String getPassword() {return password;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
}
