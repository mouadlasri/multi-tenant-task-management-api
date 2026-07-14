package org.practice.multitenanttaskmanagementapi.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByProject_IdAndDeletedAtIsNull(UUID projectId, Pageable pageable);

    Optional<Task> findByIdAndProject_IdAndDeletedAtIsNull(UUID taskId, UUID projectId);

    List<Task> findAllByProject_IdAndDeletedAtIsNull(UUID projectId);
}
