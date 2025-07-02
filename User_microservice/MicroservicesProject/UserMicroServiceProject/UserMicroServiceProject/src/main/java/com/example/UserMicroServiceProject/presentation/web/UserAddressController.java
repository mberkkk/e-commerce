package com.example.UserMicroServiceProject.presentation.web;

import com.example.UserMicroServiceProject.application.port.input.AddressServicePort;
import com.example.UserMicroServiceProject.application.usecase.command.AddAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RemoveAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.SetDefaultAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserAddressesQuery;
import com.example.UserMicroServiceProject.application.usecase.response.AddressListResponse;
import com.example.UserMicroServiceProject.application.usecase.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class UserAddressController {
    private final AddressServicePort addressService;

    // Adres ekle
    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@PathVariable String userId, @RequestBody AddAddressCommand command) {
        AddAddressCommand cmd = AddAddressCommand.builder()
                .userId(userId)
                .title(command.getTitle())
                .fullAddress(command.getFullAddress())
                .city(command.getCity())
                .district(command.getDistrict())
                .postalCode(command.getPostalCode())
                .isDefault(command.isDefault())
                .build();
        return ResponseEntity.ok(addressService.addAddress(cmd));
    }

    // Adresleri listele
    @GetMapping
    public ResponseEntity<AddressListResponse> getAddresses(@PathVariable String userId) {
        GetUserAddressesQuery query = GetUserAddressesQuery.builder().userId(userId).includeInactive(true).build();
        return ResponseEntity.ok(addressService.getUserAddresses(query));
    }

    // Adres sil
    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressResponse> removeAddress(@PathVariable String userId, @PathVariable String addressId) {
        RemoveAddressCommand command = RemoveAddressCommand.builder().userId(userId).addressId(addressId).build();
        return ResponseEntity.ok(addressService.removeAddress(command));
    }

    // Varsayılan adresi ayarla
    @PutMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefaultAddress(@PathVariable String userId, @PathVariable String addressId) {
        SetDefaultAddressCommand command = SetDefaultAddressCommand.builder().userId(userId).addressId(addressId).build();
        return ResponseEntity.ok(addressService.setDefaultAddress(command));
    }
}