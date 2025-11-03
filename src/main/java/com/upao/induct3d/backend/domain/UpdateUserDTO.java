package com.upao.induct3d.backend.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String name;
    private String surname;

    public String getName() {return name;}
    public String getSurname() {return surname;}

    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
}
