package com.upao.induct3d.backend.domain;

import java.util.Map;

public class UpdateTourRequest {
    private String voiceText;
    private Map<String, String> materialColors;

    public String getVoiceText() {
        return voiceText;
    }
    public Map<String, String> getMaterialColors() {
        return materialColors;
    }

    public void setMaterialColors(Map<String, String> materialColors) {
        this.materialColors = materialColors;
    }
    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }
}
