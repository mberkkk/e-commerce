package com.example.UserMicroServiceProject.application.service;

import com.example.UserMicroServiceProject.application.port.input.AddressServicePort;
import com.example.UserMicroServiceProject.application.port.input.EventPublisherPort;
import com.example.UserMicroServiceProject.application.port.input.UserRepositoryPort;
import com.example.UserMicroServiceProject.application.usecase.command.AddAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RemoveAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.SetDefaultAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserAddressesQuery;
import com.example.UserMicroServiceProject.application.usecase.response.AddressListResponse;
import com.example.UserMicroServiceProject.application.usecase.response.AddressResponse;
import com.example.UserMicroServiceProject.domain.event.UserAddressAddedEvent;
import com.example.UserMicroServiceProject.domain.event.UserAddressRemovedEvent;
import com.example.UserMicroServiceProject.domain.event.UserAddressUpdatedEvent;
import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.user.UserAddress;
import com.example.UserMicroServiceProject.domain.model.user.UserAddressId;
import com.example.UserMicroServiceProject.domain.model.valueobject.Address;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.service.AddressDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressApplicationService implements AddressServicePort {

    private final UserRepositoryPort userRepository;
    private final AddressDomainService addressDomainService;
    private final EventPublisherPort eventPublisher;

    @Override
    public AddressResponse addAddress(AddAddressCommand command) {
        log.info("Adding address for user: {}", command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create address value object
        Address address = Address.of(
                command.getTitle(),
                command.getFullAddress(),
                command.getCity(),
                command.getDistrict(),
                command.getPostalCode()
        );

        // Validate address using domain service
        var validationResult = addressDomainService.validateNewAddress(user, address);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(validationResult.getErrorMessage());
        }

        // Add address to user (domain logic)
        user.addAddress(address, command.isDefault());

        // Save user
        User savedUser = userRepository.save(user);

        // Get the newly added address
        UserAddress newAddress = savedUser.getAddresses().get(savedUser.getAddresses().size() - 1);

        // Publish domain event
        UserAddressAddedEvent event = UserAddressAddedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(user.getId().getId())
                .addressId(newAddress.getId().getValue())
                .addressTitle(address.getTitle())
                .city(address.getCity())
                .district(address.getDistrict())
                .isDefault(command.isDefault())
                .isBusinessAddress(address.isBusinessAddress())
                .eventTimestamp(java.time.Instant.now())
                .build();

        return AddressResponse.from(newAddress);
    }

    @Override
    public AddressListResponse getUserAddresses(GetUserAddressesQuery query) {
        log.debug("Getting addresses for user: {}", query.getUserId());

        UserId userId = UserId.of(query.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<AddressResponse> addresses = user.getAddresses().stream()
                .map(AddressResponse::from)
                .toList();

        return AddressListResponse.from(addresses);
    }

    @Override
    public AddressResponse removeAddress(RemoveAddressCommand command) {
        log.info("Removing address {} for user: {}", command.getAddressId(), command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserAddressId addressId = UserAddressId.of(command.getAddressId());

        // Find address to remove (for event)
        UserAddress addressToRemove = user.getAddresses().stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Remove address (domain logic)
        user.removeAddress(addressId);

        // Save user
        userRepository.save(user);

        // Publish domain event
        UserAddressRemovedEvent event = UserAddressRemovedEvent.create(
                user.getId().getId(),
                addressToRemove.getId().getValue(),
                addressToRemove.getAddress().getTitle(),
                addressToRemove.getAddress().getCity(),
                addressToRemove.isDefault()
        );
        return AddressResponse.from(addressToRemove);
    }

    @Override
    public AddressResponse setDefaultAddress(SetDefaultAddressCommand command) {
        log.info("Setting default address {} for user: {}", command.getAddressId(), command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserAddressId addressId = UserAddressId.of(command.getAddressId());

        // Set default address (domain logic)
        user.setDefaultAddress(addressId);

        // Save user
        userRepository.save(user);

        // Find the address that was set as default
        UserAddress defaultAddress = user.getAddresses().stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Publish domain event
        UserAddressUpdatedEvent event = UserAddressUpdatedEvent.create(
                user.getId().getId(),
                defaultAddress.getId().getValue(),
                defaultAddress.getAddress().getTitle(),
                defaultAddress.getAddress().getCity(),
                defaultAddress.getAddress().getDistrict(),
                true // isDefault
        );

        return AddressResponse.from(defaultAddress);
    }

    @Override
    public AddressResponse updateAddress(com.example.UserMicroServiceProject.application.usecase.command.UpdateAddressCommand command) {
        log.info("Updating address {} for user: {}", command.getAddressId(), command.getUserId());

        UserId userId = UserId.of(command.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserAddressId addressId = UserAddressId.of(command.getAddressId());
        UserAddress addressToUpdate = user.getAddresses().stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Yeni address value object oluştur
        Address newAddress = Address.of(
                command.getTitle(),
                command.getFullAddress(),
                command.getCity(),
                command.getDistrict(),
                command.getPostalCode()
        );

        // Domain method ile güncelle
        addressToUpdate.updateAddress(newAddress);

        // Save user
        userRepository.save(user);

        // Publish domain event (opsiyonel)
        UserAddressUpdatedEvent event = UserAddressUpdatedEvent.create(
                user.getId().getId(),
                addressToUpdate.getId().getValue(),
                newAddress.getTitle(),
                newAddress.getCity(),
                newAddress.getDistrict(),
                addressToUpdate.isDefault()
        );

        return AddressResponse.from(addressToUpdate);
    }
}