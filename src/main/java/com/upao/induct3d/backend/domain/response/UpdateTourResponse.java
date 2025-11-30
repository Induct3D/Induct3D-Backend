package com.upao.induct3d.backend.domain.response;

public class UpdateTourResponse {

    private String tourId;
    private String message;

    public UpdateTourResponse() {}

    public UpdateTourResponse(String tourId, String message) {
        this.tourId = tourId;
        this.message = message;
    }

    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

