package org.practice.multitenanttaskmanagementapi.project;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.project.dto.CreateProjectRequest;
import org.practice.multitenanttaskmanagementapi.project.dto.ProjectResponse;
import org.practice.multitenanttaskmanagementapi.project.dto.UpdateProjectRequest;
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
@RequestMapping("/api/v1/organizations/{organizationId}/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAll(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable UUID organizationId
            ) {
        User user = (User) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ProjectResponse> projectResponsePage = projectService.getAllProjects(user.getId(), organizationId, pageable);

        return ResponseEntity.ok(projectResponsePage);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId) {
        User user = (User) authentication.getPrincipal();

        ProjectResponse projectResponse = projectService.getProjectById(user.getId(), organizationId, projectId);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(Authentication authentication,@PathVariable UUID organizationId, @Valid @RequestBody CreateProjectRequest createProjectRequest) {
        User user = (User) authentication.getPrincipal();

        ProjectResponse projectResponse = projectService.createProject(user.getId(), organizationId, createProjectRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(Authentication authentication, @PathVariable  UUID organizationId, @PathVariable UUID projectId, @Valid @RequestBody UpdateProjectRequest updateProjectRequest) {
        User user = (User) authentication.getPrincipal();

        ProjectResponse projectResponse = projectService.updateProject(user.getId(), organizationId, projectId, updateProjectRequest);

        return ResponseEntity.ok(projectResponse);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID projectId) {
        User user = (User) authentication.getPrincipal();

        projectService.deleteProject(user.getId(), organizationId, projectId);

        return ResponseEntity.noContent().build();
    }
}
