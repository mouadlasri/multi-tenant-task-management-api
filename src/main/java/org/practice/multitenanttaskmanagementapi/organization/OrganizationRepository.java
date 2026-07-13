package org.practice.multitenanttaskmanagementapi.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<Organization> findByIdAndDeletedAtIsNull(UUID organizationId);

    Page<Organization> findAllByDeletedAtIsNull(Pageable pageable);
}
