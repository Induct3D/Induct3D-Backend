package com.upao.induct3d.backend.domain.response;

import com.upao.induct3d.backend.entity.Tour;

public class TourStatusItemResponse {

    private String tourId;
    private String tourName;
    private String description;
    private String status;

    public TourStatusItemResponse() {}

    public TourStatusItemResponse(String tourId, String tourName, String description, String status) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.status = status;
    }

    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }

    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static TourStatusItemResponse fromEntity(Tour t) {
        return new TourStatusItemResponse(
                t.getTourId(),
                t.getTourName(),
                t.getDescription(),
                t.getStatus() != null ? t.getStatus().name() : null
        );
    }
}

