package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ForbiddenException;

public class MembershipForbiddenException extends ForbiddenException {
    public MembershipForbiddenException() {
        super("You don't own this organization.");
    }
}
