package org.practice.multitenanttaskmanagementapi.auth;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.auth.dto.LoginRequest;
import org.practice.multitenanttaskmanagementapi.auth.dto.LoginResponse;
import org.practice.multitenanttaskmanagementapi.user.UserService;
import org.practice.multitenanttaskmanagementapi.user.dto.CreateUserRequest;
import org.practice.multitenanttaskmanagementapi.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse token = authService.login(loginRequest);

        return ResponseEntity.ok(token);
    }
}
