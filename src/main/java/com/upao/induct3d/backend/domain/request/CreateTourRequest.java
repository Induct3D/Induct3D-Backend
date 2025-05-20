package com.upao.induct3d.backend.domain.request;

import java.util.Map;

public class CreateTourRequest {
    private String templateId;
    private String tourName;
    private String description;
    private String voiceText;
    private Map<String, String> materialColors;

    public String getTemplateId() {
        return templateId;
    }
    public String getTourName() {
        return tourName;
    }
    public String getDescription() {
        return description;
    }
    public String getVoiceText() {
        return voiceText;
    }
    public Map<String, String> getMaterialColors() {
        return materialColors;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }
    public void setMaterialColors(Map<String, String> materialColors) {
        this.materialColors = materialColors;
    }

    public static class MaterialChangeDTO {
        private String name;
        private String color;

        public String getName() {
            return name;
        }
        public String getColor() {
            return color;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setColor(String color) {
            this.color = color;
        }
    }
}

