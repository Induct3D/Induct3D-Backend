package com.upao.induct3d.backend.domain.response;

import java.util.Map;

public class TourResponse {
    private String tourId;
    private String tourName;
    private String description;
    private String voiceText;
    private Map<String,String> materialColors;
    private String glbUrl;

    public TourResponse() {
    }

    public TourResponse(String tourId, String tourName, String description, String voiceText, Map<String, String> materialColors, String glbUrl) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.voiceText = voiceText;
        this.materialColors = materialColors;
        this.glbUrl = glbUrl;
    }

    // GETTERS
    public String getTourId() {
        return tourId;
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
    public String getGlbUrl() {
        return glbUrl;
    }

    // SETTERS
    public void setTourId(String tourId) {
        this.tourId = tourId;
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
    public void setGlbUrl(String glbUrl) {
        this.glbUrl = glbUrl;
    }
}
