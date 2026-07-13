package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceAlreadyExistsException;

public class MembershipAlreadyExistsException extends ResourceAlreadyExistsException {
    public MembershipAlreadyExistsException() {
        super("This user is already a member of the organization.");
    }
}
