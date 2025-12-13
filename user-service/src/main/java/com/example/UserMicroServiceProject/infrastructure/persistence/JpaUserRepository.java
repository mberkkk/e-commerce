package com.example.UserMicroServiceProject.infrastructure.persistence;


import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UserId> {
    User findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<User> findByStatus(UserStatus status);
    List<User> findByEmailVerifiedFalse();
    List<User> findByLockedUntilIsNotNull();
    long countByStatus(UserStatus status);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
