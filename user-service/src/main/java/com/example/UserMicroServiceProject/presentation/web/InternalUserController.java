package com.example.UserMicroServiceProject.presentation.web;

import com.example.UserMicroServiceProject.application.port.input.UserServicePort;
import com.example.UserMicroServiceProject.application.usecase.command.ValidateUserCommand;
import com.example.UserMicroServiceProject.application.usecase.query.GetUserByIdQuery;
import com.example.UserMicroServiceProject.application.usecase.response.UserResponse;
import com.example.UserMicroServiceProject.application.usecase.response.UserValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserServicePort userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable String id) {
        GetUserByIdQuery query = new GetUserByIdQuery(id, false);
        return ResponseEntity.ok(userService.getUserById(query));
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<UserValidationResponse> validateUser(@PathVariable String id,
                                                               @RequestParam(defaultValue = "true") boolean checkActiveStatus) {
        ValidateUserCommand command = new ValidateUserCommand(id, checkActiveStatus);
        return ResponseEntity.ok(userService.validateUser(command));
    }

    @GetMapping("/{id}/default-address")
    public ResponseEntity<?> getDefaultAddress(@PathVariable String id) {
        GetUserByIdQuery query = new GetUserByIdQuery(id, true);
        return ResponseEntity.ok(userService.getDefaultAddress(query));
    }
}