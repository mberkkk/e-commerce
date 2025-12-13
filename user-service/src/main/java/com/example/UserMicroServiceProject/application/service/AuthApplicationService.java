package com.example.UserMicroServiceProject.application.service;

import com.example.UserMicroServiceProject.application.port.input.AuthServicePort;
import com.example.UserMicroServiceProject.application.port.input.EventPublisherPort;
import com.example.UserMicroServiceProject.application.port.input.UserRepositoryPort;
import com.example.UserMicroServiceProject.application.usecase.command.LoginCommand;
import com.example.UserMicroServiceProject.application.usecase.command.LogoutCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RefreshTokenCommand;
import com.example.UserMicroServiceProject.application.usecase.response.LoginResponse;
import com.example.UserMicroServiceProject.application.usecase.response.LogoutResponse;
import com.example.UserMicroServiceProject.application.usecase.response.RefreshTokenResponse;
import com.example.UserMicroServiceProject.application.usecase.response.UserResponse;
import com.example.UserMicroServiceProject.domain.event.AccountLockedEvent;
import com.example.UserMicroServiceProject.domain.event.LoginFailedEvent;
import com.example.UserMicroServiceProject.domain.event.LoginSuccessfulEvent;
import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.service.AuthenticationDomainService;
import com.example.UserMicroServiceProject.domain.service.supporting.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthApplicationService implements AuthServicePort {

    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;
    private final AuthenticationDomainService authDomainService;
    private final JwtTokenService jwtTokenService; // Infrastructure service

    @Override
    public LoginResponse login(LoginCommand command) {
        log.info("Login attempt for email: {}", command.getEmail());

        try {
            // Find user by email
            Email email = Email.of(command.getEmail());
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user == null) {
                // Publish failed login event (no user)
                publishLoginFailedEvent(command.getEmail(), "USER_NOT_FOUND", command);
                return LoginResponse.failure("INVALID_CREDENTIALS", "Invalid email or password");
            }

            // Use domain service for authentication logic
            AuthenticationResult authResult = authDomainService.authenticate(user, command.getPassword());

            if (!authResult.isSuccessful()) {
                // Save failed attempt to user (domain logic)
                userRepository.save(user);

                // Publish failed login event
                publishLoginFailedEvent(command.getEmail(), authResult.getFailureReason().name(), command);

                // Check if account is now locked
                if (user.isLocked()) {
                    publishAccountLockedEvent(user);
                }

                return LoginResponse.failure(
                        authResult.getFailureReason().name(),
                        authResult.getMessage()
                );
            }

            // Success: Generate tokens
            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            java.time.Instant expiresAt = jwtTokenService.getTokenExpiration(accessToken);

            // Save successful login (domain logic handles this)
            userRepository.save(user);

            // Publish successful login event
            publishLoginSuccessfulEvent(user, command);

            log.info("Login successful for user: {}", user.getId().getId());

            return LoginResponse.success(
                    accessToken,
                    refreshToken,
                    expiresAt,
                    UserResponse.from(user)
            );

        } catch (Exception e) {
            log.error("Login error for email {}: {}", command.getEmail(), e.getMessage());
            publishLoginFailedEvent(command.getEmail(), "SYSTEM_ERROR", command);
            return LoginResponse.failure("SYSTEM_ERROR", "Login temporarily unavailable");
        }
    }

    @Override
    public LogoutResponse logout(LogoutCommand command) {
        log.info("Logout for user: {}", command.getUserId());

        try {
            // Invalidate token (implementation in infrastructure)
            jwtTokenService.invalidateToken(command.getToken());

            log.info("Logout successful for user: {}", command.getUserId());
            return LogoutResponse.success();

        } catch (Exception e) {
            log.error("Logout error for user {}: {}", command.getUserId(), e.getMessage());
            return LogoutResponse.failure("Logout failed: " + e.getMessage());
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenCommand command) {
        log.debug("Refreshing token");

        try {
            // Validate refresh token and get user
            String userId = jwtTokenService.validateRefreshToken(command.getRefreshToken());

            if (userId == null) {
                return RefreshTokenResponse.failure("Invalid refresh token");
            }

            // Get user and check status
            User user = userRepository.findById(UserId.of(userId))
                    .orElse(null);

            if (user == null || !user.canLogin()) {
                return RefreshTokenResponse.failure("User cannot login");
            }

            // Generate new tokens
            String newAccessToken = jwtTokenService.generateAccessToken(user);
            String newRefreshToken = jwtTokenService.generateRefreshToken(user);
            java.time.Instant expiresAt = jwtTokenService.getTokenExpiration(newAccessToken);

            return RefreshTokenResponse.success(newAccessToken, newRefreshToken, expiresAt);

        } catch (Exception e) {
            log.error("Token refresh error: {}", e.getMessage());
            return RefreshTokenResponse.failure("Token refresh failed");
        }
    }

    private void publishLoginSuccessfulEvent(User user, LoginCommand command) {
        LoginSuccessfulEvent event = LoginSuccessfulEvent.create(
                user.getId().getId(),
                user.getEmail().getValue(),
                command.getIpAddress(),
                command.getUserAgent()
        );
    }

    private void publishLoginFailedEvent(String email, String reason, LoginCommand command) {
        LoginFailedEvent event = LoginFailedEvent.create(
                email,
                reason,
                command.getIpAddress(),
                command.getUserAgent(),
                null // attempt count could be calculated
        );
    }

    private void publishAccountLockedEvent(User user) {
        AccountLockedEvent event = AccountLockedEvent.create(
                user.getId().getId(),
                user.getEmail().getValue(),
                "TOO_MANY_FAILED_ATTEMPTS",
                user.getFailedLoginAttempts(),
                user.getLockedUntil()
        );
    }
}
