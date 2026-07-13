package org.practice.multitenanttaskmanagementapi.organization.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class OrganizationNotFoundException extends ResourceNotFoundException {
    public OrganizationNotFoundException(String message) {
        super(message);
    }
}
