package com.upao.induct3d.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "templates")
public class Template {
    @Id
    private String id;

    private String name;
    private String description;
    private List<String> images; // URLs a imágenes
    private String glbUrl; // URL al archivo .glb
    private ObjectId userId; // Id del usuario que creó el template

    public Template() {
    }

    public Template(String name, String description, List<String> images, String glbUrl, ObjectId userId) {
        this.name = name;
        this.description = description;
        this.images = images;
        this.glbUrl = glbUrl;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    public String getGlbUrl() {
        return glbUrl;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setGlbUrl(String glbUrl) {
        this.glbUrl = glbUrl;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
