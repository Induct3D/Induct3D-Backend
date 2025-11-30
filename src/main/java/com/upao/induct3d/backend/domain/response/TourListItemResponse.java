package com.upao.induct3d.backend.domain.response;

public class TourListItemResponse {

    private String tourId;
    private String tourName;
    private String description;

    public TourListItemResponse() {}

    public TourListItemResponse(String tourId, String tourName, String description) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
    }

    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }

    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
