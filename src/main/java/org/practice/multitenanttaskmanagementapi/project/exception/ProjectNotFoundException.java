package org.practice.multitenanttaskmanagementapi.project.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super("Project not found.");
    }
}
