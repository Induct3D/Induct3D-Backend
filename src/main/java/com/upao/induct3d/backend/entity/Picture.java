package com.upao.induct3d.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pictures")
public class Picture {
    @Id
    private String id;

    private String publicId;
    private String url;
    private String uploadedBy;

    public Picture() {}

    public Picture(String publicId, String url, String uploadedBy) {
        this.publicId   = publicId;
        this.url        = url;
        this.uploadedBy = uploadedBy;
    }

    public String getId() { return id; }
    public String getPublicId() { return publicId; }
    public String getUrl() { return url; }
    public String getUploadedBy() { return uploadedBy; }

    public void setId(String id) { this.id = id; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public void setUrl(String url) { this.url = url; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
}
