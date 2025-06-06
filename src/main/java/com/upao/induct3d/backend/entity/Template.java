package com.upao.induct3d.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "templates")
public class Template {
    @Id
    private String id;

    private String name;
    private String description;
    private List<String> images;
    private String glbUrl;
    private ObjectId userId;
    private Vector3 userStart;
    private List<PredefinedStep> predefinedSteps;

    public Template() {
    }

    public Template(String name, String description, List<String> images, String glbUrl, ObjectId userId) {
        this.name = name;
        this.description = description;
        this.images = images;
        this.glbUrl = glbUrl;
        this.userId = userId;
    }

    public static class Vector3 {
        private double x;
        private double y;
        private double z;

        public Vector3() {}

        public Vector3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }

        public void setX(double x) { this.x = x; }
        public void setY(double y) { this.y = y; }
        public void setZ(double z) { this.z = z; }
    }

    public static class PredefinedStep {
        private String id;
        private List<Vector3> position;
        private Boolean hasBoard;
        private BoardConfig boardConfig;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public List<Vector3> getPosition() { return position; }
        public void setPosition(List<Vector3> position) { this.position = position; }

        public Boolean getHasBoard() { return hasBoard; }
        public void setHasBoard(Boolean hasBoard) { this.hasBoard = hasBoard; }

        public BoardConfig getBoardConfig() { return boardConfig; }
        public void setBoardConfig(BoardConfig boardConfig) { this.boardConfig = boardConfig; }
    }

    public static class BoardConfig {
        private Vector3 position;
        private Vector3 rotation;
        private double scale;

        public BoardConfig() {}

        public BoardConfig(Vector3 position, Vector3 rotation, double scale) {
            this.position = position;
            this.rotation = rotation;
            this.scale = scale;
        }

        public Vector3 getPosition() { return position; }
        public void setPosition(Vector3 position) { this.position = position; }

        public Vector3 getRotation() { return rotation; }
        public void setRotation(Vector3 rotation) { this.rotation = rotation; }

        public double getScale() { return scale; }
        public void setScale(double scale) { this.scale = scale; }
    }

    // GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getImages() { return images; }
    public String getGlbUrl() { return glbUrl; }
    public ObjectId getUserId() { return userId; }
    public Vector3 getUserStart() { return userStart; }
    public List<PredefinedStep> getPredefinedSteps() { return predefinedSteps; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImages(List<String> images) { this.images = images; }
    public void setGlbUrl(String glbUrl) { this.glbUrl = glbUrl; }
    public void setUserId(ObjectId userId) { this.userId = userId; }
    public void setUserStart(Vector3 userStart) { this.userStart = userStart; }
    public void setPredefinedSteps(List<PredefinedStep> predefinedSteps) { this.predefinedSteps = predefinedSteps; }

}
