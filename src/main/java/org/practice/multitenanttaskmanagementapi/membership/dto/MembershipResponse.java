package org.practice.multitenanttaskmanagementapi.membership.dto;

import org.practice.multitenanttaskmanagementapi.membership.MembershipRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MembershipResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String email;
    private MembershipRole role;
    private OffsetDateTime createdAt;

    public MembershipResponse(UUID id, UUID userId, String name, String email, MembershipRole role, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MembershipRole getRole() {
        return role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
