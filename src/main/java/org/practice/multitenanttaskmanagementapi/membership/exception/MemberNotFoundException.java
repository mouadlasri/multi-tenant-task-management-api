package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
