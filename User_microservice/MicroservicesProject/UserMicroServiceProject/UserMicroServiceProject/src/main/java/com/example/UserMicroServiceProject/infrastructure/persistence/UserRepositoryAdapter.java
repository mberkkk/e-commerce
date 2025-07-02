package com.example.UserMicroServiceProject.infrastructure.persistence;

import com.example.UserMicroServiceProject.application.port.output.UserRepositoryPort;
import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return Optional.ofNullable(jpaUserRepository.findByEmail(email));
    }

    @Override
    public void deleteById(UserId userId) {
        jpaUserRepository.deleteById(userId);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public List<User> findActiveUsers() {
        return jpaUserRepository.findByStatus(UserStatus.ACTIVE);
    }

    @Override
    public List<User> findUsersByStatus(UserStatus status) {
        return jpaUserRepository.findByStatus(status);
    }

    @Override
    public List<User> findUsersWithUnverifiedEmail() {
        return jpaUserRepository.findByEmailVerifiedFalse();
    }

    @Override
    public List<User> findLockedUsers() {
        return jpaUserRepository.findByLockedUntilIsNotNull();
    }

    @Override
    public List<User> findUsersRequiringPasswordChange() {
        // Bu metod için özel bir sorgu gerekebilir
        return List.of(); // Şimdilik boş liste dönüyoruz
    }

    @Override
    public long countActiveUsers() {
        return jpaUserRepository.countByStatus(UserStatus.ACTIVE);
    }

    @Override
    public long countUsersByRegistrationDate(LocalDate startDate, LocalDate endDate) {
        return jpaUserRepository.countByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }
}