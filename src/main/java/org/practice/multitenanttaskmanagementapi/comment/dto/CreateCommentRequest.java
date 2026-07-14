package org.practice.multitenanttaskmanagementapi.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateCommentRequest {
    @NotBlank
    private String content;

    public CreateCommentRequest() {}

    public CreateCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
