package com.example.UserMicroServiceProject.application.port.input;

import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryPort{

    // Basic CRUD Operations
    User save(User user);
    Optional<User> findById(UserId userId);
    Optional<User> findByEmail(Email email);
    void deleteById(UserId userId);

    // Business Query Operations
    boolean existsByEmail(Email email);
    List<User> findActiveUsers();
    List<User> findUsersByStatus(UserStatus status);

    // Complex Queries (Business Driven)
    List<User> findUsersWithUnverifiedEmail();
    List<User> findLockedUsers();
    List<User> findUsersRequiringPasswordChange();

    // Statistics Queries
    long countActiveUsers();
    long countUsersByRegistrationDate(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
