package com.upao.induct3d.backend.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;
    private String userId;
    private Instant expiresAt;
    private Instant revokedAt;
    private String replacedBy;

    public RefreshToken() {
    }

    public RefreshToken(String id, String userId, Instant expiresAt, Instant revokedAt, String replacedBy) {
        this.id = id;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.replacedBy = replacedBy;
    }

    public String getId() {return id;}
    public String getUserId() {return userId;}
    public Instant getExpiresAt() {return expiresAt;}
    public Instant getRevokedAt() {return revokedAt;}
    public String getReplacedBy() {return replacedBy;}

    public void setId(String id) {this.id = id;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setExpiresAt(Instant expiresAt) {this.expiresAt = expiresAt;}
    public void setRevokedAt(Instant revokedAt) {this.revokedAt = revokedAt;}
    public void setReplacedBy(String replacedBy) {this.replacedBy = replacedBy;}
}
