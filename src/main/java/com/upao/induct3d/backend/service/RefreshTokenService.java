package com.upao.induct3d.backend.service;

import com.mongodb.lang.Nullable;
import com.upao.induct3d.backend.entity.RefreshToken;
import com.upao.induct3d.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    public String issue(String userId, long days) {
        String jti = UUID.randomUUID().toString();
        repo.save(RefreshToken.builder()
                .id(jti)
                .userId(userId)
                .expiresAt(Instant.now().plus(days, ChronoUnit.DAYS))
                .build());
        return jti;
    }

    public Optional<RefreshToken> validateActive(String jti) {
        return repo.findById(jti)
                .filter(t -> t.getRevokedAt() == null && t.getExpiresAt().isAfter(Instant.now()));
    }

    public void revoke(String jti, @Nullable String replacedBy) {
        repo.findById(jti).ifPresent(t -> {
            t.setRevokedAt(Instant.now());
            t.setReplacedBy(replacedBy);
            repo.save(t);
        });
    }
}

