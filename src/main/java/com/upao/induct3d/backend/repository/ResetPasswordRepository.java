package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.ResetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends MongoRepository<ResetPassword, String> {
    Optional<ResetPassword> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}
