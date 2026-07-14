package org.practice.multitenanttaskmanagementapi.comment;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.comment.dto.CommentResponse;
import org.practice.multitenanttaskmanagementapi.comment.dto.CreateCommentRequest;
import org.practice.multitenanttaskmanagementapi.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getAllCommentsByTaskId(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable UUID organizationId,
            @PathVariable UUID projectId,
            @PathVariable UUID taskId
    ) {
        User user = (User) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CommentResponse> commentResponsePage = commentService.getAllCommentsByTaskId(user.getId(), organizationId, projectId, taskId, pageable);

        return ResponseEntity.ok(commentResponsePage);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId, @PathVariable UUID commentId) {
        User user = (User) authentication.getPrincipal();

        CommentResponse commentResponse = commentService.getCommentById(user.getId(), organizationId, projectId, taskId, commentId);

        return ResponseEntity.ok(commentResponse);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId, @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        User user = (User) authentication.getPrincipal();

        CommentResponse commentResponse = commentService.createComment(user.getId(), organizationId, projectId, taskId, createCommentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId, @PathVariable UUID commentId) {
        User user = (User) authentication.getPrincipal();

        commentService.deleteComment(user.getId(), organizationId, projectId, taskId, commentId);

        return ResponseEntity.noContent().build();
    }
}
