package com.upao.induct3d.backend.domain.request;

import com.upao.induct3d.backend.entity.Template;

import java.util.List;

public class CreateTemplateRequest {
    private String name;
    private String description;
    private String glbUrl;
    private List<String> images;
    private Template.Vector3 userStart;
    private List<Template.PredefinedStep> predefinedSteps;

    // GETTERS
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getGlbUrl() { return glbUrl; }
    public List<String> getImages() { return images; }
    public Template.Vector3 getUserStart() { return userStart; }
    public List<Template.PredefinedStep> getPredefinedSteps() { return predefinedSteps; }

    // SETTERS
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setGlbUrl(String glbUrl) { this.glbUrl = glbUrl; }
    public void setImages(List<String> images) { this.images = images; }
    public void setUserStart(Template.Vector3 userStart) { this.userStart = userStart; }
    public void setPredefinedSteps(List<Template.PredefinedStep> predefinedSteps) { this.predefinedSteps = predefinedSteps; }
}
