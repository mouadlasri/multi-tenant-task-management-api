package org.practice.multitenanttaskmanagementapi.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findAllByOrganizationIdAndDeletedAtIsNull(UUID organizationId, Pageable pageable);

    Optional<Project> findByIdAndDeletedAtIsNull(UUID projectId);

    @Query("SELECT p FROM Project p JOIN FETCH p.organization WHERE p.id = :projectId AND p.organization.id = :organizationId AND p.deletedAt IS NULL")
    Optional<Project> findByIdAndOrganization_IdAndDeletedAtIsNullWithOrganization(UUID projectId, UUID organizationId);
}
