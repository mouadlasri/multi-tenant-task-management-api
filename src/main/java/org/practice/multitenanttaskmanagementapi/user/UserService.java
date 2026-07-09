package org.practice.multitenanttaskmanagementapi.user;

import org.practice.multitenanttaskmanagementapi.user.dto.CreateUserRequest;
import org.practice.multitenanttaskmanagementapi.user.dto.UserResponse;
import org.practice.multitenanttaskmanagementapi.user.exception.EmailAlreadyExistsException;
import org.practice.multitenanttaskmanagementapi.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User getActiveUserEntityByEmail(String email) {
        return userRepository.findUserByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional(readOnly = true)
    public User getActiveUserEntityById(UUID id) {
        return userRepository.findUserByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        String name = createUserRequest.getName();
        String email = createUserRequest.getEmail();
        String password = createUserRequest.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(name, email, hashedPassword);

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
