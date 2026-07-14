package org.practice.multitenanttaskmanagementapi.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.author WHERE c.task.id = :taskId AND c.author.deletedAt IS NULL AND c.deletedAt IS NULL",
    countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.task.id = :taskId AND c.author.deletedAt IS NULL AND c.deletedAt IS NULL")
    Page<Comment> findAllByTask_IdAndDeletedAtIsNullWithAuthor(@Param("taskId") UUID taskId, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.id = :commentId AND c.task.id = :taskId AND c.author.deletedAt IS NULL AND c.deletedAt IS NULL")
    Optional<Comment> findByIdAndTask_IdAndDeletedAtIsNullWithAuthor(@Param("commentId") UUID commentId, @Param("taskId") UUID taskId);

    Optional<Comment> findByIdAndTask_IdAndDeletedAtIsNull(UUID commentId, UUID taskId);
}
