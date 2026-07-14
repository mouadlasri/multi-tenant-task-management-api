package org.practice.multitenanttaskmanagementapi.project.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class ProjectNotFoundException extends ResourceNotFoundException {
    public ProjectNotFoundException() {
        super("Project not found.");
    }
}
