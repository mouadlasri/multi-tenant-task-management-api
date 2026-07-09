package org.practice.multitenanttaskmanagementapi.user.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceAlreadyExistsException;

public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {
    public EmailAlreadyExistsException() {
        super("Email already exists.");
    }
}
