package org.practice.multitenanttaskmanagementapi.task.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {
    public TaskNotFoundException() {
        super("Task not found.");
    }
}
