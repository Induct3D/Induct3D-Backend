package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.entity.UserEntity;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.jwt.JwtProvider;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.utils.Operations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    public UserEntity create(UserDTO dto) throws AttributeException {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new AttributeException("username already in use");
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new AttributeException("email already in use");

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        return userRepository.save(user);
    }

    public JwtTokenDTO login(LoginUserDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProvider.generateToken(auth);
        return new JwtTokenDTO(token);
    }
}
