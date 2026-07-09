package org.practice.multitenanttaskmanagementapi.auth;

import org.practice.multitenanttaskmanagementapi.auth.dto.LoginRequest;
import org.practice.multitenanttaskmanagementapi.auth.dto.LoginResponse;
import org.practice.multitenanttaskmanagementapi.auth.exception.InvalidCredentialsException;
import org.practice.multitenanttaskmanagementapi.security.JwtService;
import org.practice.multitenanttaskmanagementapi.user.User;
import org.practice.multitenanttaskmanagementapi.user.UserService;
import org.practice.multitenanttaskmanagementapi.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user;

        try {
            user = userService.getActiveUserEntityByEmail(email);
        } catch (UserNotFoundException exception) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(token);
    }
}
