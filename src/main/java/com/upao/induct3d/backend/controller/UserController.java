package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UpdateUserDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.domain.request.UpdateUserProfileRequest;
import com.upao.induct3d.backend.domain.response.ApiResponse;
import com.upao.induct3d.backend.domain.response.UpdateUserProfileResponse;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.exception.AuthUnauthorizedException;
import com.upao.induct3d.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AuthUnauthorizedException("No se ha enviado un token válido");
        }
        return auth.getName();
    }

    // Get user profile
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the authenticated user's profile information.")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile() {
        String username = getCurrentUsername();
        UserDTO profile = userService.getUserProfile(username);
        return ResponseEntity.ok(new ApiResponse<>(profile));
    }

    // Update user profile
    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile information.")
    public ResponseEntity<ApiResponse<UpdateUserProfileResponse>> updateProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        String currentUsername = getCurrentUsername();
        UserDTO updatedProfile = userService.updateUserProfile(currentUsername, request);
        UpdateUserProfileResponse body = new UpdateUserProfileResponse("Perfil actualizado correctamente", updatedProfile);
        return ResponseEntity.ok(new ApiResponse<>(body));
    }

    // Delete user profile (soft delete)
    @DeleteMapping("/profile")
    @Operation(summary = "Delete user profile", description = "Deactivates the user profile and associated tours.")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteProfile() {
        String username = getCurrentUsername();
        userService.deleteUserProfile(username);
        Map<String, String> body = Map.of("message", "Perfil eliminado correctamente");
        return ResponseEntity.ok(new ApiResponse<>(body));
    }
}