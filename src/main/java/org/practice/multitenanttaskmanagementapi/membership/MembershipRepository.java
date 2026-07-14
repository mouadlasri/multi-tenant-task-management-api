package org.practice.multitenanttaskmanagementapi.membership;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    @Query(value = "SELECT mb FROM Membership mb JOIN FETCH mb.user JOIN FETCH mb.organization WHERE mb.organization.id = :organizationId AND mb.deletedAt IS NULL AND mb.user.deletedAt IS NULL AND mb.organization.deletedAt IS NULL",
    countQuery = "SELECT COUNT(mb) FROM Membership mb WHERE mb.organization.id = :organizationId AND mb.deletedAt IS NULL AND mb.user.deletedAt IS NULL AND mb.organization.deletedAt IS NULL")
    Page<Membership> findAllMembersByOrganizationIdWithUser(@Param("organizationId") UUID organizationId, Pageable pageable);

    @Query("SELECT mb FROM Membership mb JOIN FETCH mb.user WHERE mb.user.id = :userId AND mb.organization.id = :organizationId AND mb.deletedAt IS NULL AND mb.user.deletedAt IS NULL AND mb.organization.deletedAt IS NULL")
    Optional<Membership> findByOrganizationIdAndUserIdAndDeletedAtIsNull(@Param("organizationId") UUID organizationId, @Param("userId") UUID userId);

    @Query("SELECT mb FROM Membership mb JOIN FETCH mb.user WHERE mb.id = :membershipId AND mb.organization.id = :organizationId AND mb.deletedAt IS NULL AND mb.user.deletedAt IS NULL AND mb.organization.deletedAt IS NULL")
    Optional<Membership> findActiveMemberByOrganizationIdWithUser(@Param("organizationId") UUID organizationId, @Param("membershipId") UUID membershipId);

    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);

    boolean existsByIdAndOrganizationIdAndDeletedAtIsNull(UUID membershipId, UUID organizationId);

    Optional<Membership> findByIdAndOrganizationIdAndDeletedAtIsNull(UUID membershipId, UUID organizationId);

    long countByOrganization_IdAndRoleAndDeletedAtIsNull(UUID organizationId, MembershipRole role);

    List<Membership> findAllByUser_IdAndDeletedAtIsNull(UUID userId);
}
