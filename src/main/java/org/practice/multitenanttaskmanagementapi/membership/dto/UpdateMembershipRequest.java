package org.practice.multitenanttaskmanagementapi.membership.dto;

import jakarta.validation.constraints.NotNull;
import org.practice.multitenanttaskmanagementapi.membership.MembershipRole;

public class UpdateMembershipRequest {
    @NotNull
    private MembershipRole role;

    public UpdateMembershipRequest() {}

    public UpdateMembershipRequest(MembershipRole role) {
        this.role = role;
    }

    public MembershipRole getRole() {
        return role;
    }

    public void setRole(MembershipRole role) {
        this.role = role;
    }
}
