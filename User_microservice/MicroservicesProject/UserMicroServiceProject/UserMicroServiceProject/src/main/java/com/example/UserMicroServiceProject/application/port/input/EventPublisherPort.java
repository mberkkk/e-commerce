package com.example.UserMicroServiceProject.application.port.input;

import com.example.UserMicroServiceProject.domain.event.AccountLockedEvent;
import com.example.UserMicroServiceProject.domain.event.DomainEvent;
import com.example.UserMicroServiceProject.domain.event.LoginFailedEvent;
import com.example.UserMicroServiceProject.domain.event.LoginSuccessfulEvent;
import com.example.UserMicroServiceProject.domain.event.PasswordChangedEvent;
import com.example.UserMicroServiceProject.domain.event.UserActivatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserAddressAddedEvent;
import com.example.UserMicroServiceProject.domain.event.UserAddressRemovedEvent;
import com.example.UserMicroServiceProject.domain.event.UserAddressUpdatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserDeactivatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserProfileUpdatedEvent;
import com.example.UserMicroServiceProject.domain.event.UserRegisteredEvent;
import org.springframework.stereotype.Service;

@Service
public interface EventPublisherPort {

    // User Lifecycle Events
    void publishUserRegistered(UserRegisteredEvent event);
    void publishUserActivated(UserActivatedEvent event);
    void publishUserDeactivated(UserDeactivatedEvent event);

    // Profile Events
    void publishProfileUpdated(UserProfileUpdatedEvent event);
    void publishPasswordChanged(PasswordChangedEvent event);

    // Generic Event Publishing
    void publishDomainEvent(DomainEvent event);
}
