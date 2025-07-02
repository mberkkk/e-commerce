package com.example.UserMicroServiceProject.application.port.input;

import com.example.UserMicroServiceProject.application.usecase.command.LoginCommand;
import com.example.UserMicroServiceProject.application.usecase.command.LogoutCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RefreshTokenCommand;
import com.example.UserMicroServiceProject.application.usecase.response.LoginResponse;
import com.example.UserMicroServiceProject.application.usecase.response.LogoutResponse;
import com.example.UserMicroServiceProject.application.usecase.response.RefreshTokenResponse;

public interface AuthServicePort {

    // Basic Auth Operations
    LoginResponse login(LoginCommand command);
    LogoutResponse logout(LogoutCommand command);
    RefreshTokenResponse refreshToken(RefreshTokenCommand command);
}
