package org.practice.multitenanttaskmanagementapi.task;

import org.practice.multitenanttaskmanagementapi.membership.MembershipService;
import org.practice.multitenanttaskmanagementapi.organization.Organization;
import org.practice.multitenanttaskmanagementapi.organization.OrganizationService;
import org.practice.multitenanttaskmanagementapi.project.Project;
import org.practice.multitenanttaskmanagementapi.project.ProjectRepository;
import org.practice.multitenanttaskmanagementapi.project.ProjectService;
import org.practice.multitenanttaskmanagementapi.project.exception.ProjectNotFoundException;
import org.practice.multitenanttaskmanagementapi.task.dto.CreateTaskRequest;
import org.practice.multitenanttaskmanagementapi.task.dto.TaskResponse;
import org.practice.multitenanttaskmanagementapi.task.dto.UpdateTaskRequest;
import org.practice.multitenanttaskmanagementapi.task.exception.TaskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final MembershipService membershipService;
    private final ProjectService projectService;
    private final OrganizationService organizationService;

    public TaskService(TaskRepository taskRepository, MembershipService membershipService, ProjectService projectService, OrganizationService organizationService) {
        this.taskRepository = taskRepository;
        this.membershipService = membershipService;
        this.projectService = projectService;
        this.organizationService = organizationService;
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasksByProjectId(UUID userId, UUID organizationId, UUID projectId, Pageable pageable) {
        membershipService.requireMember(userId, organizationId);

        projectService.getProjectEntityById(userId, organizationId, projectId);

        Page<Task> taskPage = taskRepository.findAllByProject_IdAndDeletedAtIsNull(projectId, pageable);

        Page<TaskResponse> taskResponsePage = taskPage.map(task -> toTaskResponse(task));

        return taskResponsePage;
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID userId, UUID organizationId, UUID projectId, UUID taskId) {
        membershipService.requireMember(userId, organizationId);

        projectService.getProjectEntityById(userId, organizationId, projectId);

        Task task = taskRepository.findByIdAndProject_IdAndDeletedAtIsNull(taskId, projectId).orElseThrow(() -> new TaskNotFoundException());

        return toTaskResponse(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskEntityById(UUID userId, UUID organizationId, UUID projectId, UUID taskId) {
        membershipService.requireMember(userId, organizationId);

        projectService.getProjectEntityById(userId, organizationId, projectId);

        Task task = taskRepository.findByIdAndProject_IdAndDeletedAtIsNull(taskId, projectId).orElseThrow(() -> new TaskNotFoundException());

        return task;
    }

    @Transactional
    public TaskResponse createTask(UUID userId, UUID organizationId, UUID projectId, CreateTaskRequest createTaskRequest) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        Project project = projectService.getProjectEntityById(userId, organizationId, projectId);

        String title = createTaskRequest.getTitle();
        String description = createTaskRequest.getDescription();

        Task task = new Task(project, title, description, TaskStatus.TODO);

        Task savedTask = taskRepository.save(task);

        return toTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(UUID userId, UUID organizationId, UUID projectId, UUID taskId, UpdateTaskRequest updateTaskRequest) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        projectService.getProjectEntityById(userId, organizationId, projectId);

        Task task = taskRepository.findByIdAndProject_IdAndDeletedAtIsNull(taskId, projectId)
                .orElseThrow(() -> new TaskNotFoundException());

        String title = updateTaskRequest.getTitle();
        String description = updateTaskRequest.getDescription();
        TaskStatus status = updateTaskRequest.getStatus();

        if (title != null && !task.getTitle().equals(title)) {
            task.setTitle(title);
        }

        if (description != null && !description.equals(task.getDescription())) {
            task.setDescription(description);
        }

        if (status != null && !task.getStatus().equals(status)) {
            task.setStatus(status);
        }

        return toTaskResponse(task);
    }

    @Transactional
    public void deleteTask(UUID userId, UUID organizationId, UUID projectId, UUID taskId) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        projectService.getProjectEntityById(userId, organizationId, projectId);

        Task task = taskRepository.findByIdAndProject_IdAndDeletedAtIsNull(taskId, projectId)
                .orElseThrow(() -> new TaskNotFoundException());

        task.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @Transactional
    public void softDeleteAllTasksByProjectId(UUID projectId) {
        List<Task> tasks = taskRepository.findAllByProject_IdAndDeletedAtIsNull(projectId);

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        for (Task task : tasks) {
            task.setDeletedAt(now);
        }
    }


    private TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

}
