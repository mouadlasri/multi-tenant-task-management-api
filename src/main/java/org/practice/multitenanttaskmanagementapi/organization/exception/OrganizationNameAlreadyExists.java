package org.practice.multitenanttaskmanagementapi.organization.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceAlreadyExistsException;

public class OrganizationNameAlreadyExists extends ResourceAlreadyExistsException {
    public OrganizationNameAlreadyExists(String message) {
        super(message);
    }
}
