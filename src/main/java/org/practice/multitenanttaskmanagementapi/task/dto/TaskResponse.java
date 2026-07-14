package org.practice.multitenanttaskmanagementapi.task.dto;

import org.practice.multitenanttaskmanagementapi.task.TaskStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public TaskResponse(UUID id, String title, String description, TaskStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
