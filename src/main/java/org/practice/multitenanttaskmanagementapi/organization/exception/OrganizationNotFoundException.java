package org.practice.multitenanttaskmanagementapi.organization.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class OrganizationNotFoundException extends ResourceNotFoundException {
    public OrganizationNotFoundException() {
        super("Organization not found.");
    }
}
