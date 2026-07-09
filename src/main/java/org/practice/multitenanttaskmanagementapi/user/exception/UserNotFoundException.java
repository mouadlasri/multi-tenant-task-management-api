package org.practice.multitenanttaskmanagementapi.user.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
