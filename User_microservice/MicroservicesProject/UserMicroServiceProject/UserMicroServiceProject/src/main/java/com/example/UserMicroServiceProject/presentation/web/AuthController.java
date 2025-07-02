package com.example.UserMicroServiceProject.presentation.web;

import com.example.UserMicroServiceProject.application.port.input.AuthServicePort;
import com.example.UserMicroServiceProject.application.usecase.command.LoginCommand;
import com.example.UserMicroServiceProject.application.usecase.command.LogoutCommand;
import com.example.UserMicroServiceProject.application.usecase.command.RefreshTokenCommand;
import com.example.UserMicroServiceProject.application.usecase.response.LoginResponse;
import com.example.UserMicroServiceProject.application.usecase.response.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServicePort authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginCommand command) {
        return ResponseEntity.ok(authService.login(command));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);
        return ResponseEntity.ok(authService.refreshToken(command));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        LogoutCommand command = LogoutCommand.builder().token(token).build();
        authService.logout(command);
        return ResponseEntity.ok().build();
    }
}