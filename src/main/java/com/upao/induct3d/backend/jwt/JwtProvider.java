package com.upao.induct3d.backend.jwt;

import com.upao.induct3d.backend.service.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${app.jwt.secretBase64}")
    private String secretBase64;

    @Value("${app.jwt.access.minutes:15}")
    private long accessMinutes;

    @Value("${app.jwt.refresh.days:15}")
    private long refreshDays;

    private Key key() {
        byte[] bytes = Decoders.BASE64.decode(secretBase64);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Authentication authentication) {
        return generateAccessToken(authentication);
    }

    public String generateAccessToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username;
        Collection<? extends GrantedAuthority> authorities;

        if (principal instanceof UserDetails ud) {
            username = ud.getUsername();
            authorities = ud.getAuthorities();
        } else {
            username = String.valueOf(principal);
            authorities = Collections.emptyList();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        if (authorities != null) {
            claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }

        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(accessMinutes, ChronoUnit.MINUTES));

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key())
                .compact();
    }

    public String generateRefreshToken(String username, String jti) {
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(refreshDays, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(username)
                .setId(jti)
                .claim("type", "refresh")
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key())
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return parse(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims c = parse(token);
            Date exp = c.getExpiration();
            if (exp == null || exp.before(new Date())) return false;

            Object type = c.get("type");
            if (type != null && !"access".equals(type)) return false;

            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            Object t = parse(token).get("type");
            return "access".equals(t);
        } catch (Exception e) {
            return false;
        }
    }
}