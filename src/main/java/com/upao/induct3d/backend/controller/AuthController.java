package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.domain.request.ResetRequestDTO;
import com.upao.induct3d.backend.domain.response.ResetPasswordDTO;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.service.EmailService;
import com.upao.induct3d.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired UserService userService;
    @Autowired private EmailService emailService;

    // Create user
    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a new user in the system and returns a success message.")
    public ResponseEntity<MessageDTO> create(@Valid @RequestBody UserDTO dto) throws AttributeException {
        User user = userService.create(dto);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "user " + user.getUsername() + " have been created"));
    }

    // Login
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    public ResponseEntity<JwtTokenDTO> login(@Valid @RequestBody LoginUserDTO dto) throws AttributeException {
        JwtTokenDTO jwtTokenDTO = userService.login(dto);
        return ResponseEntity.ok(jwtTokenDTO);
    }

    // Request reset password
    @PostMapping("/reset-request")
    @Operation(summary = "Request password reset", description = "Sends a password reset code to the user's email.")
    public ResponseEntity<?> requestReset(@RequestBody ResetRequestDTO dto) {
        userService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Código enviado a tu correo"));
    }

    // Validate verification code
    @GetMapping("/validate-code/{email}/{code}")
    @Operation(summary = "Validate reset code", description = "Validates the password reset code for the given email.")
    public ResponseEntity<MessageDTO> validateCode(@PathVariable String email, @PathVariable String code) {
        boolean isValid = userService.validateResetCode(email, code);
        if (isValid) {
            return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Código válido"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(HttpStatus.BAD_REQUEST, "Código inválido"));
        }
    }

    // Reset password
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets the user's password using the provided reset code.")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto.getEmail(), dto.getCode(), dto.getNewPassword());
        emailService.sendPasswordChangeNotification(dto.getEmail(), LocalDateTime.now());
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Contraseña actualizada correctamente"));
    }

    //Validate token
    @GetMapping("/validate-token")
    @Operation(summary = "Validate JWT token", description = "Validates if the provided JWT token is still active and valid.")
    public ResponseEntity<MessageDTO> validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO(HttpStatus.UNAUTHORIZED, "Token not provided"));
        }

        try {
            boolean isValid = userService.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Token is valid"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageDTO(HttpStatus.UNAUTHORIZED, "Token is invalid or expired"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO(HttpStatus.UNAUTHORIZED, "Token validation failed"));
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }
}
