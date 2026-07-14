package org.practice.multitenanttaskmanagementapi.comment;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.practice.multitenanttaskmanagementapi.task.Task;
import org.practice.multitenanttaskmanagementapi.user.User;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public Comment() {}

    public Comment(Task task, User author, String content) {
        this.task = task;
        this.author = author;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public User getAuthor() {
        return author;
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

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
