package com.upao.induct3d.backend.entity;

import org.springframework.data.annotation.Id;

public abstract class EntityId {

    @Id
    protected int id;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}
