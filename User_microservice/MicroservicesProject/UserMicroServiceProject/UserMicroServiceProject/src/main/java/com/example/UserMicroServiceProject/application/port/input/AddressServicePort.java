package com.example.UserMicroServiceProject.application.port.input;

import com.example.UserMicroServiceProject.application.usecase.command.AddAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RemoveAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.command.SetDefaultAddressCommand;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserAddressesQuery;
import com.example.UserMicroServiceProject.application.usecase.response.AddressListResponse;
import com.example.UserMicroServiceProject.application.usecase.response.AddressResponse;

public interface AddressServicePort {

    // Basic Address Operations
    AddressResponse addAddress(AddAddressCommand command);
    AddressListResponse getUserAddresses(GetUserAddressesQuery query);
    AddressResponse removeAddress(RemoveAddressCommand command);
    AddressResponse setDefaultAddress(SetDefaultAddressCommand command);
    AddressResponse updateAddress(com.example.UserMicroServiceProject.application.usecase.command.UpdateAddressCommand command);
}

