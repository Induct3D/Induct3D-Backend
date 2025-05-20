package com.upao.induct3d.backend.domain.request;

import java.util.Map;

public class UpdateTourRequest {
    private String voiceText;
    private String tourName;
    private String description;
    private Map<String, String> materialColors;

    // GETTERS
    public String getVoiceText() {
        return voiceText;
    }
    public String getTourName() {
        return tourName;
    }
    public String getDescription() {
        return description;
    }
    public Map<String, String> getMaterialColors() {
        return materialColors;
    }

    //SETTERS
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setMaterialColors(Map<String, String> materialColors) {
        this.materialColors = materialColors;
    }
    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }
}
