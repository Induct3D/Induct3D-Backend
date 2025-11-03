package com.upao.induct3d.backend.repository;

import com.upao.induct3d.backend.entity.User;
import com.upao.induct3d.backend.entity.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRole(UserRole role);

    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByRole(UserRole role);

    List<User> findAllByRole(UserRole role);
}
