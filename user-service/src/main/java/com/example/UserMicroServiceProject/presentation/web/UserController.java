package com.example.UserMicroServiceProject.presentation.web;

import com.example.UserMicroServiceProject.application.port.input.UserServicePort;
import com.example.UserMicroServiceProject.application.usecase.command.*;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserByIdQuery;
import com.example.UserMicroServiceProject.application.usecase.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServicePort userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserCommand command) {
        return ResponseEntity.ok(userService.registerUser(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id,
                                                    @RequestParam(defaultValue = "false") boolean includeAddresses) {
        GetUserByIdQuery query = new GetUserByIdQuery(id, includeAddresses);
        return ResponseEntity.ok(userService.getUserById(query));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable String id,
                                                      @RequestBody UpdateUserProfileCommand command) {
        command.setUserId(id);
        return ResponseEntity.ok(userService.updateUserProfile(command));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable String id,
                                            @RequestBody ChangePasswordCommand command) {
        ChangePasswordCommand.builder().userId(id).build();
        return ResponseEntity.ok(userService.changePassword(command));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable String id) {
        ActivateUserCommand command = ActivateUserCommand.builder().userId(id).build();
        return ResponseEntity.ok(userService.activateUser(command));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable String id,
                                                       @RequestParam String reason) {
        DeactivateUserCommand command = DeactivateUserCommand.builder().userId(id).reason(reason).build();
        return ResponseEntity.ok(userService.deactivateUser(command));
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<?> validateUser(@PathVariable String id,
                                          @RequestParam(defaultValue = "true") boolean checkActiveStatus) {
        ValidateUserCommand command = new ValidateUserCommand(id, checkActiveStatus);
        return ResponseEntity.ok(userService.validateUser(command));
    }
}