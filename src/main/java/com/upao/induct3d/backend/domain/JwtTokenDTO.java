package com.upao.induct3d.backend.domain;

import com.upao.induct3d.backend.entity.UserRole;

public class JwtTokenDTO {
    private String token;
    private UserRole role;

    public JwtTokenDTO() {}

    public JwtTokenDTO(String token, UserRole role) {this.token = token; this.role = role;}

    public String getToken() {return token;}
    public UserRole getRole() {return role;}

    public void setToken(String token) {this.token = token;}
    public void setRole(UserRole role) {this.role = role;}
}
