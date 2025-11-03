package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UpdateUserDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Get user profile
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the authenticated user's profile information.")
    public ResponseEntity<UserDTO> getProfile() {
        String username = getCurrentUsername();
        UserDTO profile = userService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    // Update user profile
    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile information.")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateUserDTO dto) throws AttributeException {
        String username = getCurrentUsername();
        UserDTO updated = userService.updateUserProfile(username, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete user profile (soft delete)
    @DeleteMapping("/profile")
    @Operation(summary = "Delete user profile", description = "Deactivates the user profile and associated tours.")
    public ResponseEntity<MessageDTO> deleteProfile() {
        String username = getCurrentUsername();
        userService.deleteUserProfile(username);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Perfil eliminado correctamente"));
    }
}