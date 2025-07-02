package com.example.UserMicroServiceProject.application.service;

import com.example.UserMicroServiceProject.application.port.input.UserServicePort;
import com.example.UserMicroServiceProject.application.port.output.EventPublisherPort;
import com.example.UserMicroServiceProject.application.port.output.UserRepositoryPort;
import com.example.UserMicroServiceProject.application.usecase.command.ActivateUserCommand;
import com.example.UserMicroServiceProject.application.usecase.command.ChangePasswordCommand;
import com.example.UserMicroServiceProject.application.usecase.command.DeactivateUserCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RegisterUserCommand;
import com.example.UserMicroServiceProject.application.usecase.command.UpdateUserProfileCommand;
import com.example.UserMicroServiceProject.application.usecase.command.ValidateUserCommand;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserByIdQuery;
import com.example.UserMicroServiceProject.application.usecase.response.AddressResponse;
import com.example.UserMicroServiceProject.application.usecase.response.PasswordChangeResponse;
import com.example.UserMicroServiceProject.application.usecase.response.UserResponse;
import com.example.UserMicroServiceProject.application.usecase.response.UserValidationResponse;
import com.example.UserMicroServiceProject.domain.event.PasswordChangedEvent;
import com.example.UserMicroServiceProject.domain.event.UserActivatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserDeactivatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserProfileUpdatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserRegisteredEvent;
import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.Password;
import com.example.UserMicroServiceProject.domain.model.valueobject.PersonName;
import com.example.UserMicroServiceProject.domain.model.valueobject.PhoneNumber;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserApplicationService implements UserServicePort {

    // Output Ports - Dependencies injected via constructor
    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;
    private final UserDomainService userDomainService;

    @Override
    public UserResponse registerUser(RegisterUserCommand command) {
        log.info("Registering new user with email: {}", command.getEmail());

        // Create Value Objects from command (validation happens here)
        Email email = Email.of(command.getEmail());
        Password password = Password.fromRawPassword(command.getPassword());
        PersonName name = PersonName.of(command.getFirstName(), command.getLastName());

        // Optional phone number
        PhoneNumber phoneNumber = null;
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().trim().isEmpty()) {
            phoneNumber = PhoneNumber.of(command.getPhoneNumber());
        }

        // Check if email already exists (business rule)
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email.getValue() + " already exists");
        }

        // Use Domain Service to create user (complex business logic)
        User user = phoneNumber != null
                ? userDomainService.createUser(email, password, name, phoneNumber)
                : userDomainService.createUser(email, password, name);

        // Save user through repository port
        User savedUser = userRepository.save(user);

        // Publish domain event (async communication)
        UserRegisteredEvent event = UserRegisteredEvent.from(savedUser);
        eventPublisher.publishUserRegistered(event);

        log.info("User registered successfully with ID: {}", savedUser.getId().getId());

        return UserResponse.from(savedUser);
    }

    @Override
    public UserResponse getUserById(GetUserByIdQuery query) {
        log.debug("Getting user by ID: {}", query.getUserId());

        UserId userId = UserId.of(query.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + query.getUserId()));

        if (query.isIncludeAddresses()) {
            return UserResponse.fromWithAddresses(user);
        }

        return UserResponse.from(user);
    }

    @Override
    public UserResponse updateUserProfile(UpdateUserProfileCommand command) {
        log.info("Updating profile for user: {}", command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create new value objects
        PersonName newName = PersonName.of(command.getFirstName(), command.getLastName());
        PhoneNumber newPhoneNumber = command.getPhoneNumber() != null
                ? PhoneNumber.of(command.getPhoneNumber())
                : null;

        // Domain entity handles business logic
        user.updateProfile(newName, newPhoneNumber);

        // Save changes
        User updatedUser = userRepository.save(user);

        // Publish domain event
        UserProfileUpdatedEvent event = UserProfileUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(user.getId().getId())
                .changedFields(java.util.Map.of(
                        "firstName", command.getFirstName(),
                        "lastName", command.getLastName(),
                        "phoneNumber", command.getPhoneNumber()))
                .updatedBy(command.getUserId()) // Self-update
                .eventTimestamp(java.time.Instant.now())
                .build();

        // eventPublisher.publishProfileUpdated(event);

        return UserResponse.from(updatedUser);
    }

    @Override
    public PasswordChangeResponse changePassword(ChangePasswordCommand command) {
        log.info("Changing password for user: {}", command.getUserId());

        try {
            UserId userId = UserId.of(command.getUserId());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Create new password value object (validates strength)
            Password newPassword = Password.fromRawPassword(command.getNewPassword());

            // Domain entity handles password change logic
            user.changePassword(command.getCurrentPassword(), newPassword);

            // Save changes
            userRepository.save(user);

            // Publish security event
            PasswordChangedEvent event = PasswordChangedEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .userId(user.getId().getId())
                    .eventTimestamp(java.time.Instant.now())
                    .build();

            // eventPublisher.publishPasswordChanged(event);

            log.info("Password changed successfully for user: {}", command.getUserId());

            return PasswordChangeResponse.success();

        } catch (IllegalArgumentException e) {
            log.warn("Password change failed for user {}: {}", command.getUserId(), e.getMessage());
            return PasswordChangeResponse.failure(e.getMessage());
        }
    }

    @Override
    public UserResponse activateUser(ActivateUserCommand command) {
        log.info("Activating user: {}", command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Domain entity handles activation logic
        user.activate();

        User activatedUser = userRepository.save(user);

        // Publish domain event
        UserActivatedEvent event = UserActivatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(user.getId().getId())
                .activatedBy("SYSTEM") // Could be admin ID in real scenario
                .eventTimestamp(java.time.Instant.now())
                .build();

        // eventPublisher.publishUserActivated(event);

        return UserResponse.from(activatedUser);
    }

    @Override
    public UserResponse deactivateUser(DeactivateUserCommand command) {
        log.info("Deactivating user: {} with reason: {}", command.getUserId(), command.getReason());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Domain entity handles deactivation logic
        user.deactivate();

        User deactivatedUser = userRepository.save(user);

        // Publish domain event
        UserDeactivatedEvent event = UserDeactivatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(user.getId().getId())
                .reason(command.getReason())
                .deactivatedBy("SYSTEM")
                .eventTimestamp(java.time.Instant.now())
                .build();

        // eventPublisher.publishUserDeactivated(event);

        return UserResponse.from(deactivatedUser);
    }

    @Override
    public UserValidationResponse validateUser(ValidateUserCommand command) {
        log.debug("Validating user: {}", command.getUserId());

        try {
            UserId userId = UserId.of(command.getUserId());
            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user == null) {
                return UserValidationResponse.invalid("User not found");
            }

            if (command.isCheckActiveStatus() && !user.isActive()) {
                return UserValidationResponse.invalid("User is not active");
            }

            return UserValidationResponse.valid(
                    user.getId().getId(),
                    user.getEmail().getValue(),
                    user.getName().getFullName());

        } catch (Exception e) {
            log.error("Error validating user {}: {}", command.getUserId(), e.getMessage());
            return UserValidationResponse.invalid("Validation error: " + e.getMessage());
        }
    }

    @Override
    public AddressResponse getDefaultAddress(GetUserByIdQuery query) {
        log.debug("Getting default address for user: {}", query.getUserId());

        UserId userId = UserId.of(query.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + query.getUserId()));

        return user.getDefaultAddress()
                .map(address -> AddressResponse.builder()
                        .addressId(address.getId().getValue())
                        .city(address.getAddress().getCity())
                        .postalCode(address.getAddress().getPostalCode())
                        .isDefault(address.isDefault())
                        .build())
                .orElse(null);
    }

}