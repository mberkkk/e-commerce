package com.example.UserMicroServiceProject.application.port.input;

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

public interface UserServicePort {

    // User Management Operations
    UserResponse registerUser(RegisterUserCommand command);
    UserResponse getUserById(GetUserByIdQuery query);
    UserResponse updateUserProfile(UpdateUserProfileCommand command);
    UserResponse deactivateUser(DeactivateUserCommand command);
    UserResponse activateUser(ActivateUserCommand command);

    // Profile Management
    PasswordChangeResponse changePassword(ChangePasswordCommand command);

    // User Validation (for other services)
    UserValidationResponse validateUser(ValidateUserCommand command);

    // Internal Service Operations
    AddressResponse getDefaultAddress(GetUserByIdQuery query);

}
