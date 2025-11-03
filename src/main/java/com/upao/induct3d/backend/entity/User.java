package com.upao.induct3d.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String surname;

    @Field("role")
    private UserRole role = UserRole.CREATOR;

    public User() {}

    public User(ObjectId id, String username, String email, String password, String name, String surname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public ObjectId getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public UserRole getRole() {return role;}

    public void setId(ObjectId id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setRole(UserRole role) {this.role = role;}
}
