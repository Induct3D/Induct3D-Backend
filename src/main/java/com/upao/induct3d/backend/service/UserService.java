package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.UpdateUserDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.entity.ResetPassword;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.jwt.JwtProvider;
import com.upao.induct3d.backend.repository.ResetPasswordRepository;
import com.upao.induct3d.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TourService tourService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ResetPasswordRepository resetPasswordRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager, EmailService emailService, TourService tourService ,ResetPasswordRepository resetPasswordRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.tourService = tourService;
        this.resetPasswordRepository = resetPasswordRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    ///////////////---------- ACTIONS USER -----------/////////////////////

    // Create user
    public User create(UserDTO dto) throws AttributeException {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new AttributeException("username already in use");
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new AttributeException("email already in use");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        return userRepository.save(user);
    }

    // Get user profile
    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isActive()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return new UserDTO(user);
    }

    // Update user profile
    public UserDTO updateUserProfile(String username, UpdateUserDTO dto) throws AttributeException {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isActive()) {
            throw new RuntimeException("Usuario inactivo");
        }

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());

        User updated = userRepository.save(user);
        return new UserDTO(updated);
    }

    // Soft delete user profile
    public void deleteUserProfile(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setActive(false);
        userRepository.save(user);
        tourService.deactivateToursByUser(user.getId());
    }

    ///////////////---------- ACCOUNT USER -----------/////////////////////

    // Login
    public JwtTokenDTO login(LoginUserDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        String token = jwtProvider.generateToken(auth);
        return new JwtTokenDTO(token, userPrincipal.getRole());
    }

    // Request reset password
    public void requestPasswordReset(String email) {
        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));

        String code = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        resetPasswordRepository.deleteByEmail(email); // Limpia códigos anteriores
        resetPasswordRepository.save(new ResetPassword(email, code, expiration));

        emailService.sendEmail(new String[]{email}, "Código de recuperación", code);
    }

    // Validate reset code
    public boolean validateResetCode(String email, String code) {
        ResetPassword token = resetPasswordRepository.findByEmailAndCode(email, code)
                .orElse(null);

        if (token == null || token.getExpiration().isBefore(LocalDateTime.now())) {
            return false; // Código inválido o expirado
        }
        return true; // Código válido
    }

    // Reset password
    public void resetPassword(String email, String code, String newPassword) {
        ResetPassword token = resetPasswordRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (token.getExpiration().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Código expirado");

        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordRepository.deleteByEmail(email);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            if (token != null && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                userDetailsServiceImpl.loadUserByUsername(username);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
