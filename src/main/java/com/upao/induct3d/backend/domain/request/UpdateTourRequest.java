package com.upao.induct3d.backend.domain.request;

import com.upao.induct3d.backend.entity.Tour;
import java.util.List;
import java.util.Map;

public class UpdateTourRequest {
    private String voiceText;
    private String tourName;
    private String description;
    private Map<String, String> materialColors;
    private List<Tour.Step> steps;

    // GETTERS
    public String getVoiceText() {return voiceText;}
    public String getTourName() {return tourName;}
    public String getDescription() {return description;}
    public Map<String, String> getMaterialColors() {return materialColors;}
    public List<Tour.Step> getSteps() {return steps;}

    //SETTERS
    public void setTourName(String tourName) {this.tourName = tourName;}
    public void setDescription(String description) {this.description = description;}
    public void setMaterialColors(Map<String, String> materialColors) {this.materialColors = materialColors;}
    public void setVoiceText(String voiceText) {this.voiceText = voiceText;}
    public void setSteps(List<Tour.Step> steps) {this.steps = steps;}

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
