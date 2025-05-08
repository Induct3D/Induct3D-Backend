package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.entity.UserEntity;
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

    @PostMapping("/create")
    public ResponseEntity<MessageDTO> create(@Valid @RequestBody UserDTO dto) throws AttributeException {
        UserEntity userEntity = userService.create(dto);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "user " + userEntity.getUsername() + " have been created"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> login(@Valid @RequestBody LoginUserDTO dto) throws AttributeException {
        JwtTokenDTO jwtTokenDTO = userService.login(dto);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
