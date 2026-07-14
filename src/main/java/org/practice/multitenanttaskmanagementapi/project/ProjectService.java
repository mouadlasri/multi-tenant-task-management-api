package org.practice.multitenanttaskmanagementapi.project;

import org.practice.multitenanttaskmanagementapi.membership.MembershipService;
import org.practice.multitenanttaskmanagementapi.organization.Organization;
import org.practice.multitenanttaskmanagementapi.organization.OrganizationService;
import org.practice.multitenanttaskmanagementapi.project.dto.CreateProjectRequest;
import org.practice.multitenanttaskmanagementapi.project.dto.ProjectResponse;
import org.practice.multitenanttaskmanagementapi.project.dto.UpdateProjectRequest;
import org.practice.multitenanttaskmanagementapi.project.exception.ProjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MembershipService membershipService;
    private final OrganizationService organizationService;

    public ProjectService(ProjectRepository projectRepository, MembershipService membershipService, OrganizationService organizationService) {
        this.projectRepository = projectRepository;
        this.membershipService = membershipService;
        this.organizationService = organizationService;
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAllProjects(UUID userId, UUID organizationId, Pageable pageable) {
        membershipService.requireMember(userId, organizationId);
        Page<Project> projectPage = projectRepository.findAllByOrganizationIdAndDeletedAtIsNull(organizationId, pageable);

        Page<ProjectResponse> projectResponses = projectPage.map(project -> toProjectResponse(project));

        return projectResponses;
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID userId, UUID organizationId, UUID projectId) {
        membershipService.requireMember(userId, organizationId);

        Project project = projectRepository.findByIdAndOrganization_IdAndDeletedAtIsNullWithOrganization(projectId, organizationId)
                .orElseThrow(() -> new ProjectNotFoundException());

        return toProjectResponse(project);
    }

    @Transactional(readOnly = true)
    public Project getProjectEntityById(UUID userId, UUID organizationId, UUID projectId) {
        membershipService.requireMember(userId, organizationId);

        Project project = projectRepository.findByIdAndOrganization_IdAndDeletedAtIsNullWithOrganization(projectId, organizationId)
                .orElseThrow(() -> new ProjectNotFoundException());

        return project;
    }

    @Transactional
    public ProjectResponse createProject(UUID userId, UUID organizationId, CreateProjectRequest createProjectRequest) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        String name = createProjectRequest.getName();
        String description = createProjectRequest.getDescription();

        Organization organization = organizationService.getOrganizationEntityById(organizationId);

        Project project = new Project(organization, name, description);

        Project savedProject = projectRepository.save(project);

        return toProjectResponse(savedProject);
    }

    @Transactional
    public ProjectResponse updateProject(UUID userId, UUID organizationId, UUID projectId, UpdateProjectRequest updateProjectRequest) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        Project project = projectRepository.findByIdAndOrganization_IdAndDeletedAtIsNullWithOrganization(projectId, organizationId)
                .orElseThrow(() -> new ProjectNotFoundException());

        String name = updateProjectRequest.getName();
        String description = updateProjectRequest.getDescription();

        if (name != null && !project.getName().equals(name)) {
            project.setName(name);
        }

        if (description != null && !description.equals(project.getDescription())) {
            project.setDescription(description);
        }

        return toProjectResponse(project);
    }

    @Transactional
    public void deleteProject(UUID userId, UUID organizationId, UUID projectId) {
        membershipService.requireOwnerOrAdmin(userId, organizationId);

        Project project = projectRepository.findByIdAndOrganization_IdAndDeletedAtIsNullWithOrganization(projectId, organizationId)
                .orElseThrow(() -> new ProjectNotFoundException());

        // TODO: all tasks should be deleted once the project they are assigned to is deleted

        project.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
