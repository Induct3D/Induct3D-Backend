package com.upao.induct3d.backend.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateUserProfileRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String surname;

    private String role;

    // GETTERS
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getRole() { return role; }

    // SETTERS
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setRole(String role) { this.role = role; }
}

