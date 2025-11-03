package com.upao.induct3d.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "tours")
public class Tour {

    @Id
    private String tourId;

    private ObjectId templateId;
    private ObjectId userId;
    private String tourName;
    private String description;
    @JsonIgnore private String password;
    private boolean hasPassword;
    @Indexed private TourStatus status;
    private List<ReviewNote> reviewHistory;
    private Map<String, String> materialColors;
    private List<Step> steps;

    // GETTERS
    public String getTourId() {return tourId;}
    public ObjectId getTemplateId() {return templateId;}
    public ObjectId getUserId() {return userId;}
    public String getTourName() {return tourName;}
    public String getDescription() {return description;}
    public String getPassword() {return password;}
    public boolean isHasPassword() {return hasPassword;}
    public TourStatus getStatus() {return status;}
    public List<ReviewNote> getReviewHistory() { return reviewHistory; }
    public Map<String, String> getMaterialColors() {return materialColors;}
    public List<Step> getSteps() {return steps;}

    // SETTERS
    public void setTourId(String tourId) {this.tourId = tourId;}
    public void setTemplateId(ObjectId templateId) {this.templateId = templateId;}
    public void setUserId(ObjectId userId) {this.userId = userId;}
    public void setTourName(String tourName) {this.tourName = tourName;}
    public void setDescription(String description) {this.description = description;}
    public void setPassword(String password) {this.password = password;}
    public void setHasPassword(boolean hasPassword) {this.hasPassword = hasPassword;}
    public void setStatus(TourStatus status) {this.status = status;}
    public void setReviewHistory(List<ReviewNote> reviewHistory) { this.reviewHistory = reviewHistory; }
    public void setMaterialColors(Map<String, String> materialColors) {this.materialColors = materialColors;}
    public void setSteps(List<Step> steps) {this.steps = steps;}

    // REVIEW NOTE
    public static class ReviewNote {
        private String rejectionReason;
        private LocalDateTime reviewedAt;

        public ReviewNote() {}
        public ReviewNote(String rejectionReason, LocalDateTime reviewedAt) {
            this.rejectionReason = rejectionReason;
            this.reviewedAt = reviewedAt;
        }

        public String getRejectionReason() { return rejectionReason; }
        public LocalDateTime getReviewedAt() { return reviewedAt; }

        public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
        public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    }

    // MATERIAL CHANGE
    public static class MaterialChange {
        private String name;
        private String color;

        public String getName() {return name;}
        public String getColor() {return color;}

        public void setName(String name) {this.name = name;}
        public void setColor(String color) {this.color = color;}
    }

    // STEP
    public static class Step {
        private String stepId;
        private List<String> messages;
        private BoardMedia boardMedia;

        public String getStepId() {return stepId;}
        public void setStepId(String stepId) {this.stepId = stepId;}
        public List<String> getMessages() {return messages;}

        public void setMessages(List<String> messages) {this.messages = messages;}
        public BoardMedia getBoardMedia() {return boardMedia;}
        public void setBoardMedia(BoardMedia boardMedia) {this.boardMedia = boardMedia;}
    }

    // BOARD MEDIA
    public static class BoardMedia {
        private String html;

        public String getHtml() {return html;}
        public void setHtml(String html) {this.html = html;}
    }
}

