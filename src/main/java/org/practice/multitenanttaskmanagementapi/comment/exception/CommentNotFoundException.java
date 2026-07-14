package org.practice.multitenanttaskmanagementapi.comment.exception;

import org.practice.multitenanttaskmanagementapi.exception.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super("Comment not found.");
    }
}
