package com.upao.induct3d.backend.controller;

import com.upao.induct3d.backend.domain.JwtTokenDTO;
import com.upao.induct3d.backend.domain.LoginUserDTO;
import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.UserDTO;
import com.upao.induct3d.backend.domain.request.ResetRequestDTO;
import com.upao.induct3d.backend.domain.response.ResetPasswordDTO;
import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.exception.AttributeException;
import com.upao.induct3d.backend.jwt.JwtProvider;
import com.upao.induct3d.backend.repository.UserRepository;
import com.upao.induct3d.backend.service.EmailService;
import com.upao.induct3d.backend.service.RefreshTokenService;
import com.upao.induct3d.backend.service.UserDetailsServiceImpl;
import com.upao.induct3d.backend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired UserService userService;
    @Autowired private EmailService emailService;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private RefreshTokenService refreshTokens;
    @Autowired private UserRepository userRepository;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Value("${app.jwt.refresh.days:15}")
    private long refreshDays;

    // Create user
    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a new user in the system and returns a success message.")
    public ResponseEntity<MessageDTO> create(@Valid @RequestBody UserDTO dto) throws AttributeException {
        User user = userService.create(dto);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "user " + user.getUsername() + " have been created"));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO dto) {
        var auth = userService.authenticate(dto);
        var ud = (UserDetails) auth.getPrincipal();

        String access = jwtProvider.generateAccessToken(auth);

        User user = userRepository.findByUsernameOrEmail(ud.getUsername(), ud.getUsername())
                .orElseThrow();
        String userId = user.getId().toString();
        String jti = refreshTokens.issue(userId, refreshDays);
        String refresh = jwtProvider.generateRefreshToken(ud.getUsername(), jti);

        ResponseCookie cookie = ResponseCookie.from("rt", refresh)
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/auth").maxAge(Duration.ofDays(refreshDays)).build();

        JwtTokenDTO tokenResponse = new JwtTokenDTO(access, user.getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "rt", required = false) String refreshCookie) {
        if (refreshCookie == null) return unauthorized("Missing refresh token");

        Claims c;
        try { c = jwtProvider.parse(refreshCookie); }
        catch (JwtException e) { return unauthorized("Invalid refresh token"); }

        if (!"refresh".equals(c.get("type"))) return unauthorized("Invalid token type");
        String jti = c.getId();
        var tokenRow = refreshTokens.validateActive(jti).orElse(null);
        if (tokenRow == null) return unauthorized("Revoked or expired refresh");

        // Rotar refresh
        String username = c.getSubject();
        String userId = tokenRow.getUserId();
        String newJti = refreshTokens.issue(userId, refreshDays);
        refreshTokens.revoke(jti, newJti);

        String newRefresh = jwtProvider.generateRefreshToken(username, newJti);
        var ud = userDetailsService.loadUserByUsername(username);
        Authentication tmpAuth =
                new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        String newAccess = jwtProvider.generateAccessToken(tmpAuth);

        ResponseCookie cookie = ResponseCookie.from("rt", newRefresh)
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/auth").maxAge(Duration.ofDays(refreshDays)).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("token", newAccess));
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
        if (token == null) return unauthorizedMsg("Token not provided");

        try {
            String username = jwtProvider.getUsernameFromToken(token);
            var ud = userDetailsService.loadUserByUsername(username);
            boolean ok = jwtProvider.isAccessToken(token);
            return ok
                    ? ResponseEntity.ok(new MessageDTO(HttpStatus.OK, "Token is valid"))
                    : unauthorizedMsg("Token is invalid or expired");
        } catch (Exception e) {
            return unauthorizedMsg("Token validation failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name="rt", required=false) String refreshCookie) {
        if (refreshCookie != null) {
            try {
                Claims c = jwtProvider.parse(refreshCookie);
                if ("refresh".equals(c.get("type"))) refreshTokens.revoke(c.getId(), null);
            } catch (JwtException ignored) {}
        }
        ResponseCookie clear = ResponseCookie.from("rt", "")
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/auth").maxAge(0).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clear.toString())
                .body(Map.of("message", "Logged out"));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }

    private ResponseEntity<MessageDTO> unauthorizedMsg(String msg) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDTO(HttpStatus.UNAUTHORIZED, msg));
    }

    private ResponseEntity<?> unauthorized(String msg) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "UNAUTHORIZED", "message", msg));
    }
}
