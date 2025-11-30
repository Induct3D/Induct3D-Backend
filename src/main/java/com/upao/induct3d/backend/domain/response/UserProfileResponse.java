package com.upao.induct3d.backend.domain.response;

public class UserProfileResponse {

    private String username;
    private String email;
    private String name;
    private String surname;
    private String role;

    public UserProfileResponse() {}

    public UserProfileResponse(String username, String email, String name, String surname, String role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    // GETTERS
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getRole() { return role; }

    // SETTERS
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setRole(String role) { this.role = role; }
}

