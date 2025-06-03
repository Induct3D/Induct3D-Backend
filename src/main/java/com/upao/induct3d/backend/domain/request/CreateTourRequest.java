package com.upao.induct3d.backend.domain.request;

import com.upao.induct3d.backend.entity.Tour;
import java.util.List;
import java.util.Map;

public class CreateTourRequest {
    private String templateId;
    private String tourName;
    private String description;
    private String voiceText;
    private Map<String, String> materialColors;
    private List<Tour.Step> steps;

    public String getTemplateId() {return templateId;}
    public String getTourName() {return tourName;}
    public String getDescription() {return description;}
    public String getVoiceText() {return voiceText;}
    public Map<String, String> getMaterialColors() {return materialColors;}
    public List<Tour.Step> getSteps() {return steps;}

    public void setTemplateId(String templateId) {this.templateId = templateId;}
    public void setTourName(String tourName) {this.tourName = tourName;}
    public void setDescription(String description) {this.description = description;}
    public void setVoiceText(String voiceText) {this.voiceText = voiceText;}
    public void setMaterialColors(Map<String, String> materialColors) {this.materialColors = materialColors;}
    public void setSteps(List<Tour.Step> steps) {this.steps = steps;}

    public static class MaterialChangeDTO {
        private String name;
        private String color;

        public String getName() {return name;}
        public String getColor() {return color;}

        public void setName(String name) {this.name = name;}
        public void setColor(String color) {this.color = color;}
    }

    public static class Step {
        private String stepId;
        private List<String> messages;
        private BoardMedia boardMedia;

        public String getStepId() {return stepId;}
        public List<String> getMessages() {return messages;}
        public BoardMedia getBoardMedia() {return boardMedia;}

        public void setStepId(String stepId) {this.stepId = stepId;}
        public void setMessages(List<String> messages) {this.messages = messages;}
        public void setBoardMedia(BoardMedia boardMedia) {this.boardMedia = boardMedia;}
    }

    public static class BoardMedia {
        private String html;

        public String getHtml() {return html;}
        public void setHtml(String html) {this.html = html;}
    }
}

