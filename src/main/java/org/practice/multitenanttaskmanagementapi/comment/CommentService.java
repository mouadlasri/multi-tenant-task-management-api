package org.practice.multitenanttaskmanagementapi.comment;

import org.practice.multitenanttaskmanagementapi.comment.dto.CommentResponse;
import org.practice.multitenanttaskmanagementapi.comment.dto.CreateCommentRequest;
import org.practice.multitenanttaskmanagementapi.comment.exception.CommentNotFoundException;
import org.practice.multitenanttaskmanagementapi.membership.MembershipService;
import org.practice.multitenanttaskmanagementapi.project.ProjectService;
import org.practice.multitenanttaskmanagementapi.task.Task;
import org.practice.multitenanttaskmanagementapi.task.TaskService;
import org.practice.multitenanttaskmanagementapi.user.User;
import org.practice.multitenanttaskmanagementapi.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final MembershipService membershipService;

    public CommentService(CommentRepository commentRepository, TaskService taskService, UserService userService, MembershipService membershipService) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
        this.membershipService = membershipService;
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getAllCommentsByTaskId(UUID userId, UUID organizationId, UUID projectId, UUID taskId, Pageable pageable) {
        taskService.getTaskEntityById(userId, organizationId, projectId, taskId);

        Page<Comment> commentPage = commentRepository.findAllByTask_IdAndDeletedAtIsNullWithAuthor(taskId, pageable);

        Page<CommentResponse> commentResponsePage = commentPage.map(comment -> toCommentResponse(comment));

        return commentResponsePage;
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(UUID userId, UUID organizationId, UUID projectId, UUID taskId, UUID commentId) {
        taskService.getTaskEntityById(userId, organizationId, projectId, taskId);

        Comment comment = commentRepository.findByIdAndTask_IdAndDeletedAtIsNullWithAuthor(commentId, taskId)
                .orElseThrow(() -> new CommentNotFoundException());

        return toCommentResponse(comment);
    }

    @Transactional
    public CommentResponse createComment(UUID userId, UUID organizationId, UUID projectId, UUID taskId, CreateCommentRequest createCommentRequest) {
        Task task = taskService.getTaskEntityById(userId, organizationId, projectId, taskId);
        User author = userService.getActiveUserEntityById(userId);

        String content = createCommentRequest.getContent();

        Comment comment = new Comment(task, author, content);

        Comment savedComment = commentRepository.save(comment);

        return toCommentResponse(savedComment);
    }

    @Transactional
    public void deleteComment(UUID userId, UUID organizationId, UUID projectId, UUID taskId, UUID commentId) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);
        taskService.getTaskEntityById(userId, organizationId, projectId, taskId);

        Comment comment = commentRepository.findByIdAndTask_IdAndDeletedAtIsNull(commentId, taskId)
                .orElseThrow(() -> new CommentNotFoundException());

        comment.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

}
