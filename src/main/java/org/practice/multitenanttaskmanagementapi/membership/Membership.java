package org.practice.multitenanttaskmanagementapi.membership;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.practice.multitenanttaskmanagementapi.organization.Organization;
import org.practice.multitenanttaskmanagementapi.user.User;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "memberships")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipRole role;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public Membership() {}

    public Membership(User user, Organization organization, MembershipRole role) {
        this.user = user;
        this.organization = organization;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public MembershipRole getRole() {
        return role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setRole(MembershipRole role) {
        this.role = role;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
