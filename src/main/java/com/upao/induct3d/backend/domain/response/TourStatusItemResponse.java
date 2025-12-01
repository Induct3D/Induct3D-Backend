package com.upao.induct3d.backend.domain.response;

import com.upao.induct3d.backend.entity.Tour;
import com.upao.induct3d.backend.entity.TourStatus;

public class TourStatusItemResponse {

    private String tourId;
    private String tourName;
    private String description;
    private String status;
    private String rejectionReason;

    public TourStatusItemResponse() {}

    public TourStatusItemResponse(String tourId, String tourName, String description, String status, String rejectionReason) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public String getTourId() { return tourId; }
    public String getTourName() { return tourName; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }

    public void setTourId(String tourId) { this.tourId = tourId; }
    public void setTourName(String tourName) { this.tourName = tourName; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public static TourStatusItemResponse fromEntity(Tour tour) {
        TourStatusItemResponse response = new TourStatusItemResponse();
        response.setTourId(tour.getTourId());
        response.setTourName(tour.getTourName());
        response.setDescription(tour.getDescription());
        response.setStatus(tour.getStatus() != null ? tour.getStatus().toString() : "PENDING");

        if (tour.getStatus() == TourStatus.REJECTED && tour.getReviewHistory() != null) {
            response.setRejectionReason(tour.getReviewHistory()
                    .stream()
                    .filter(note -> !note.getRejectionReason().equals("Tour aprobado"))
                    .map(Tour.ReviewNote::getRejectionReason)
                    .reduce((first, second) -> second) // Obtener el último
                    .orElse(null));
        }
        return response;
    }
}

