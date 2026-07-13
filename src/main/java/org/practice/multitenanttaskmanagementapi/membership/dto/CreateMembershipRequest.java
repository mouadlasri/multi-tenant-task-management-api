package org.practice.multitenanttaskmanagementapi.membership.dto;

import jakarta.validation.constraints.NotNull;
import org.practice.multitenanttaskmanagementapi.membership.MembershipRole;

import java.util.UUID;

public class CreateMembershipRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private MembershipRole role;

    public CreateMembershipRequest() {}

    public CreateMembershipRequest(UUID userId , MembershipRole role) {
        this.userId = userId;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public MembershipRole getRole() {
        return role;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setRole(MembershipRole role) {
        this.role = role;
    }
}
