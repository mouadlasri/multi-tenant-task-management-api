package org.practice.multitenanttaskmanagementapi.organization;

import org.practice.multitenanttaskmanagementapi.membership.Membership;
import org.practice.multitenanttaskmanagementapi.membership.MembershipRepository;
import org.practice.multitenanttaskmanagementapi.membership.MembershipRole;
import org.practice.multitenanttaskmanagementapi.membership.MembershipService;
import org.practice.multitenanttaskmanagementapi.organization.dto.CreateOrganizationRequest;
import org.practice.multitenanttaskmanagementapi.organization.dto.OrganizationResponse;
import org.practice.multitenanttaskmanagementapi.organization.dto.UpdateOrganizationRequest;
import org.practice.multitenanttaskmanagementapi.organization.exception.OrganizationNameAlreadyExists;
import org.practice.multitenanttaskmanagementapi.organization.exception.OrganizationNotFoundException;
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
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final UserService userService;
    private final MembershipService membershipService;


    public OrganizationService(OrganizationRepository organizationRepository, MembershipRepository membershipRepository, UserService userService, MembershipService membershipService) {
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
        this.userService = userService;
        this.membershipService = membershipService;
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(UUID organizationId) {
        Organization organization = organizationRepository.findByIdAndDeletedAtIsNull(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id " + organizationId));

        return toOrganizationResponse(organization);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAllOrganizations(Pageable pageable) {
        Page<Organization> organizationPage = organizationRepository.findAllByDeletedAtIsNull(pageable);

        Page<OrganizationResponse> organizationResponsePage = organizationPage.map(organization -> toOrganizationResponse(organization));

        return organizationResponsePage;
    }

    @Transactional
    public OrganizationResponse createOrganization(UUID userId, CreateOrganizationRequest createOrganizationRequest) {
        User user = userService.getActiveUserEntityById(userId);
        String name = createOrganizationRequest.getName();
        String description = createOrganizationRequest.getDescription();
        if (organizationRepository.existsByNameAndDeletedAtIsNull(name)) {
            throw new OrganizationNameAlreadyExists("Invalid organization name.");
        }

        Organization organization = new Organization(name, description);
        Organization savedOrganization = organizationRepository.save(organization);

        Membership membership = new Membership(
                user, organization, MembershipRole.OWNER
        );

        membershipRepository.save(membership);

        return toOrganizationResponse(savedOrganization);
    }

    @Transactional
    public OrganizationResponse updateOrganization(UUID userId, UUID organizationId, UpdateOrganizationRequest updateOrganizationRequest) {
        Organization organization = organizationRepository.findByIdAndDeletedAtIsNull(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id " + organizationId));

        membershipService.requireOwner(userId, organizationId);

        String newName = updateOrganizationRequest.getName();

        if (newName != null && !newName.equals(organization.getName())) {
            if (organizationRepository.existsByNameAndDeletedAtIsNull(newName)) {
                throw new OrganizationNameAlreadyExists("Invalid organization name.");
            }

            organization.setName(newName);
        }

        if (updateOrganizationRequest.getDescription() != null) {
            organization.setDescription(updateOrganizationRequest.getDescription());
        }

        return toOrganizationResponse(organization);
    }

    @Transactional
    public void deleteOrganization(UUID userId, UUID organizationId) {
        Organization organization = organizationRepository.findByIdAndDeletedAtIsNull(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id " + organizationId));

        membershipService.requireOwner(userId, organizationId);

        organization.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private OrganizationResponse toOrganizationResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }

}
