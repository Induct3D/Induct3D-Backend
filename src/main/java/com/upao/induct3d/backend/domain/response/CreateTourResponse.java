package com.upao.induct3d.backend.domain.response;

public class CreateTourResponse {
    private String tourId;
    private String tourName;
    private String description;
    private String status;

    public CreateTourResponse() {}

    public CreateTourResponse(String tourId, String tourName, String description, String status) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.status = status;
    }

    public String getTourId() {return tourId;}
    public String getTourName() {return tourName;}
    public String getDescription() {return description;}
    public String getStatus() {return status;}

    public void setTourId(String tourId) {this.tourId = tourId;}
    public void setTourName(String tourName) {this.tourName = tourName;}
    public void setDescription(String description) {this.description = description;}
    public void setStatus(String status) {this.status = status;}
}
