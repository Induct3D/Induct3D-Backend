package com.upao.induct3d.backend.domain.response;

public class UpdateTourResponse {

    private String tourId;
    private String message;
    private String password;

    public UpdateTourResponse() {}

    public UpdateTourResponse(String tourId, String message, String password) {
        this.tourId = tourId;
        this.message = message;
        this.password = password;
    }

    public String getTourId() { return tourId; }
    public String getMessage() { return message; }
    public String getPassword() { return password; }

    public void setTourId(String tourId) { this.tourId = tourId; }
    public void setMessage(String message) { this.message = message; }
    public void setPassword(String password) { this.password = password; }
}

