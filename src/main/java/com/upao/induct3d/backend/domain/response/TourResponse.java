package com.upao.induct3d.backend.domain.response;

import com.upao.induct3d.backend.entity.Template;
import com.upao.induct3d.backend.entity.Tour;
import java.util.List;
import java.util.Map;

public class TourResponse {
    private String tourId;
    private String tourName;
    private String description;
    private Map<String,String> materialColors;
    private String glbUrl;
    private List<Tour.Step> steps;
    private Vector3 userStart;
    private List<PredefinedStep> predefinedSteps;

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
        private boolean hasBoard;

        public PredefinedStep() {}

        public PredefinedStep(String id, List<Vector3> position, boolean hasBoard) {
            this.id = id;
            this.position = position;
            this.hasBoard = hasBoard;
        }

        public String getId() { return id; }
        public List<Vector3> getPosition() { return position; }
        public boolean isHasBoard() { return hasBoard; }

        public void setId(String id) { this.id = id; }
        public void setPosition(List<Vector3> position) { this.position = position; }
        public void setHasBoard(boolean hasBoard) { this.hasBoard = hasBoard; }
    }

    public TourResponse() {}
    public TourResponse(String tourId, String tourName, String description, Map<String, String> materialColors, String glbUrl, List<Tour.Step> steps, Vector3 userStart, List<PredefinedStep> predefinedSteps) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.materialColors = materialColors;
        this.glbUrl = glbUrl;
        this.steps = steps;
        this.userStart = userStart;
        this.predefinedSteps = predefinedSteps;
    }

    // GETTERS
    public String getTourId() { return tourId; }
    public String getTourName() { return tourName; }
    public String getDescription() { return description; }
    public Map<String, String> getMaterialColors() { return materialColors; }
    public String getGlbUrl() { return glbUrl; }
    public List<Tour.Step> getSteps() { return steps; }
    public Vector3 getUserStart() { return userStart; }
    public List<PredefinedStep> getPredefinedSteps() { return predefinedSteps; }

    // SETTERS
    public void setTourId(String tourId) { this.tourId = tourId; }
    public void setTourName(String tourName) { this.tourName = tourName; }
    public void setDescription(String description) { this.description = description; }
    public void setMaterialColors(Map<String, String> materialColors) { this.materialColors = materialColors; }
    public void setGlbUrl(String glbUrl) { this.glbUrl = glbUrl; }
    public void setSteps(List<Tour.Step> steps) { this.steps = steps; }
    public void setUserStart(Vector3 userStart) { this.userStart = userStart; }
    public void setPredefinedSteps(List<PredefinedStep> predefinedSteps) { this.predefinedSteps = predefinedSteps; }
}
