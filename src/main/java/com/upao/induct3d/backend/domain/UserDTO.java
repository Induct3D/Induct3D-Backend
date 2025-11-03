package com.upao.induct3d.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

    @NotBlank(message = "username is mandatory")
    private String username;
    @NotBlank(message = "email is mandatory")
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "username is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "surname is mandatory")
    private String surname;

    private UserRole role;

    public UserDTO() {}

    public UserDTO(String username, String email, String password, String name, String surname, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
    }

    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public UserRole getRole() { return role; }

    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setRole(UserRole role) { this.role = role; }

}
