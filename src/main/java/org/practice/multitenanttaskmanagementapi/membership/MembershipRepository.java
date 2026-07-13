package org.practice.multitenanttaskmanagementapi.membership;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    @Query(value = "SELECT mb FROM Membership mb JOIN FETCH mb.user JOIN FETCH mb.organization WHERE mb.organization.id = :organizationId",
    countQuery = "SELECT COUNT(mb) FROM Membership mb WHERE mb.organization.id = :organizationId")
    Page<Membership> findAllMembersByOrganizationIdWithUser(@Param("organizationId") UUID organizationId);

    @Query("SELECT mb FROM Membership mb JOIN FETCH mb.user WHERE mb.user.id = :userId AND mb.organization.id = :organizationId AND mb.deletedAt IS NULL")
    Optional<Membership> findByOrganizationIdAndUserIdAndDeletedAtIsNull(@Param("organizationId") UUID organizationId, @Param("userId") UUID userId);

    @Query("SELECT mb FROM Membership mb JOIN FETCH mb.user WHERE mb.user.id = :userId AND mb.organization.id = :organizationId")
    Optional<Membership> findMemberByOrganizationIdWithUser(@Param("organizationId") UUID organizationId, @Param("userId") UUID userId);

    @Query("SELECT mb FROM Membership mb JOIN FETCH mb.user WHERE mb.user.id = :userId AND mb.organization.id = :organizationId AND mb.user.deletedAt IS NOT NULL")
    Optional<Membership> findActiveMemberByOrganizationIdWithUser(@Param("organizationId") UUID organizationId, @Param("userId") UUID userId);
}
