package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ForbiddenException;

public class MembershipForbiddenException extends ForbiddenException {
    public MembershipForbiddenException() {
        super("You do not have permission to access this organization.");
    }
}
