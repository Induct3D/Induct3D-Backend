package com.upao.induct3d.backend.domain.response;

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

    public TourResponse() {}
    public TourResponse(String tourId, String tourName, String description, Map<String, String> materialColors, String glbUrl, List<Tour.Step> steps) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.materialColors = materialColors;
        this.glbUrl = glbUrl;
        this.steps = steps;
    }

    // GETTERS
    public String getTourId() { return tourId; }
    public String getTourName() { return tourName; }
    public String getDescription() { return description; }
    public Map<String, String> getMaterialColors() { return materialColors; }
    public String getGlbUrl() { return glbUrl; }
    public List<Tour.Step> getSteps() { return steps; }

    // SETTERS
    public void setTourId(String tourId) { this.tourId = tourId; }
    public void setTourName(String tourName) { this.tourName = tourName; }
    public void setDescription(String description) { this.description = description; }
    public void setMaterialColors(Map<String, String> materialColors) { this.materialColors = materialColors; }
    public void setGlbUrl(String glbUrl) { this.glbUrl = glbUrl; }
    public void setSteps(List<Tour.Step> steps) { this.steps = steps; }
}
