package org.practice.multitenanttaskmanagementapi.comment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private UUID authorId;
    private String authorName;
    private String content;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CommentResponse(UUID id, UUID authorId, String authorName, String content, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
