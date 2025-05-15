package com.upao.induct3d.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "tours")
public class Tour {

    @Id
    private String tourId;

    private ObjectId templateId;
    private ObjectId userId;
    private String tourName;
    private String description;
    private String voiceText;
    private Map<String, String> materialColors;

    // GETTERS
    public String getTourId() {
        return tourId;
    }
    public ObjectId getTemplateId() {
        return templateId;
    }
    public ObjectId getUserId() {
        return userId;
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

    // SETTERS
    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
    public void setTemplateId(ObjectId templateId) {
        this.templateId = templateId;
    }
    public void setUserId(ObjectId userId) {
        this.userId = userId;
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

    public static class MaterialChange {
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

