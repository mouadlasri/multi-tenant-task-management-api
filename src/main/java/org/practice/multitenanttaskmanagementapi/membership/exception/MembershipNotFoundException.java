package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

import java.util.UUID;

public class MembershipNotFoundException extends ResourceNotFoundException {
    public MembershipNotFoundException(UUID membershipId) {
        super("Membership not found with id " + membershipId);
    }
}
