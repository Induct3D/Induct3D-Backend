package com.upao.induct3d.backend.domain.response;

import com.upao.induct3d.backend.entity.Template;

import java.util.List;

public class TemplateResponse {

    private String id;
    private String name;
    private String description;
    private List<String> images;
    private String glbUrl;

    public TemplateResponse() {}

    public TemplateResponse(String id, String name, String description,
                            List<String> images, String glbUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.images = images;
        this.glbUrl = glbUrl;
    }

    // getters y setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getImages() { return images; }
    public String getGlbUrl() { return glbUrl; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImages(List<String> images) { this.images = images; }
    public void setGlbUrl(String glbUrl) { this.glbUrl = glbUrl; }

    public static TemplateResponse fromEntity(Template template) {
        return new TemplateResponse(
                template.getId(),
                template.getName(),
                template.getDescription(),
                template.getImages(),
                template.getGlbUrl()
        );
    }
}
