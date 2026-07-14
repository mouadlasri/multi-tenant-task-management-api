package org.practice.multitenanttaskmanagementapi.task.dto;

import org.practice.multitenanttaskmanagementapi.task.TaskStatus;

public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;

    public UpdateTaskRequest() {}

    public UpdateTaskRequest(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
