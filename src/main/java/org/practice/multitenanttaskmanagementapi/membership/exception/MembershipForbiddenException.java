package org.practice.multitenanttaskmanagementapi.membership.exception;

import org.practice.multitenanttaskmanagementapi.exception.ForbiddenException;

public class MembershipForbiddenException extends ForbiddenException {
    public MembershipForbiddenException(String message) {
        super(message);
    }
}
