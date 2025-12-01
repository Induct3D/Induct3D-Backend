package com.upao.induct3d.backend.domain.response;

import com.upao.induct3d.backend.domain.UserDTO;

public class UpdateUserProfileResponse {

    private String message;
    private UserDTO profile;

    public UpdateUserProfileResponse() {}

    public UpdateUserProfileResponse(String message, UserDTO profile) {
        this.message = message;
        this.profile = profile;
    }

    public String getMessage() { return message; }
    public UserDTO getProfile() { return profile; }

    public void setMessage(String message) { this.message = message; }
    public void setProfile(UserDTO profile) { this.profile = profile; }
}

