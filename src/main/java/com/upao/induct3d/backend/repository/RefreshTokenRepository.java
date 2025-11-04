package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

}
