package org.practice.multitenanttaskmanagementapi.task;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.task.dto.CreateTaskRequest;
import org.practice.multitenanttaskmanagementapi.task.dto.TaskResponse;
import org.practice.multitenanttaskmanagementapi.task.dto.UpdateTaskRequest;
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
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasksByProjectId(Authentication authentication, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @PathVariable UUID organizationId, @PathVariable UUID projectId) {
        User user = (User) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<TaskResponse> taskResponsePage = taskService.getAllTasksByProjectId(user.getId(), organizationId, projectId, pageable);

        return ResponseEntity.ok(taskResponsePage);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId) {
        User user = (User) authentication.getPrincipal();

        TaskResponse taskResponse = taskService.getTaskById(user.getId(), organizationId, projectId, taskId);

        return ResponseEntity.ok(taskResponse);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @Valid @RequestBody CreateTaskRequest createTaskRequest) {
        User user = (User) authentication.getPrincipal();

        TaskResponse taskResponse = taskService.createTask(user.getId(), organizationId, projectId, createTaskRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        User user = (User) authentication.getPrincipal();

        TaskResponse taskResponse = taskService.updateTask(user.getId(), organizationId, projectId, taskId, updateTaskRequest);

        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId, @PathVariable UUID taskId) {
        User user = (User) authentication.getPrincipal();

        taskService.deleteTask(user.getId(), organizationId, projectId, taskId);

        return ResponseEntity.noContent().build();
    }
}
