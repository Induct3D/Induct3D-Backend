package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.domain.request.ResetRequestDTO;
import com.upao.induct3d.backend.domain.response.ResetPasswordDTO;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;

    // Create user
    @PostMapping("/create")
    public ResponseEntity<MessageDTO> create(@Valid @RequestBody UserDTO dto) throws AttributeException {
        User user = userService.create(dto);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "user " + user.getUsername() + " have been created"));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> login(@Valid @RequestBody LoginUserDTO dto) throws AttributeException {
        JwtTokenDTO jwtTokenDTO = userService.login(dto);
        return ResponseEntity.ok(jwtTokenDTO);
    }

    // Request reset password
    @PostMapping("/reset-request")
    public ResponseEntity<?> requestReset(@RequestBody ResetRequestDTO dto) {
        userService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Código enviado a tu correo"));
    }

    // Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto.getEmail(), dto.getCode(), dto.getNewPassword());
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Contraseña actualizada correctamente"));
    }
}
