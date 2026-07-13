package org.practice.multitenanttaskmanagementapi.membership;

import org.practice.multitenanttaskmanagementapi.membership.dto.CreateMembershipRequest;
import org.practice.multitenanttaskmanagementapi.membership.dto.MembershipResponse;
import org.practice.multitenanttaskmanagementapi.membership.dto.UpdateMembershipRequest;
import org.practice.multitenanttaskmanagementapi.membership.exception.MemberNotFoundException;
import org.practice.multitenanttaskmanagementapi.membership.exception.MembershipAlreadyExistsException;
import org.practice.multitenanttaskmanagementapi.membership.exception.MembershipForbiddenException;
import org.practice.multitenanttaskmanagementapi.membership.exception.MembershipNotFoundException;
import org.practice.multitenanttaskmanagementapi.organization.Organization;
import org.practice.multitenanttaskmanagementapi.organization.OrganizationRepository;
import org.practice.multitenanttaskmanagementapi.organization.exception.OrganizationNotFoundException;
import org.practice.multitenanttaskmanagementapi.user.User;
import org.practice.multitenanttaskmanagementapi.user.UserRepository;
import org.practice.multitenanttaskmanagementapi.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public MembershipService(MembershipRepository membershipRepository, OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<MembershipResponse> getAllMembersByOrganizationId(UUID userId, UUID organizationId, Pageable pageable) {
        this.requireOwnerOrAdmin(userId, organizationId);

        Page<Membership> organizationMembersPage = membershipRepository.findAllMembersByOrganizationIdWithUser(organizationId, pageable);

        Page<MembershipResponse> organizationMembersResponsePage = organizationMembersPage.map(organizationMembers -> toMembershipResponse(organizationMembers));

        return organizationMembersResponsePage;
    }

    @Transactional(readOnly = true)
    public MembershipResponse getMemberByOrganizationId(UUID userId, UUID membershipId, UUID organizationId) {
        this.requireOwnerOrAdmin(userId, organizationId);

        Membership membership = membershipRepository.findActiveMemberByOrganizationIdWithUser(organizationId, membershipId)
                .orElseThrow(() -> new MemberNotFoundException());

        return toMembershipResponse(membership);
    }

    @Transactional
    public MembershipResponse createMembership(UUID userId, UUID organizationId, CreateMembershipRequest createMembershipRequest) {
        requireOwner(userId, organizationId);
        UUID newMemberId = createMembershipRequest.getUserId();
        MembershipRole newMemberRole = createMembershipRequest.getRole();

        if (membershipRepository.existsByUserIdAndOrganizationId(newMemberId, organizationId)) {
            throw new MembershipAlreadyExistsException();
        }

        // (TODO: update delete user service method to soft-delete all user's memberships as well)

        User user = userRepository.findUserByIdAndDeletedAtIsNull(newMemberId).orElseThrow(() -> new UserNotFoundException());
        Organization organization = organizationRepository.findByIdAndDeletedAtIsNull(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException());

        Membership membership = new Membership(user, organization, newMemberRole);

        Membership newMembership = membershipRepository.save(membership);

        return toMembershipResponse(newMembership);
    }

    @Transactional
    public MembershipResponse updateMembership(UUID userId, UUID organizationId, UUID membershipId, UpdateMembershipRequest updatedMembershipRequest) {
        requireOwner(userId, organizationId);

        Membership membership = membershipRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(membershipId, organizationId)
                .orElseThrow(() -> new MembershipNotFoundException(membershipId));

        MembershipRole newRole = updatedMembershipRequest.getRole();

        if (membership.getRole().equals(MembershipRole.OWNER) && !newRole.equals(MembershipRole.OWNER) && membershipRepository.countByOrganization_IdAndRoleAndDeletedAtIsNull(organizationId, MembershipRole.OWNER) == 1) {
            throw new MembershipForbiddenException("You cannot change the role of the sole owner of an organization.");
        }

        if (!newRole.equals(membership.getRole())) {
            membership.setRole(newRole);
        }

        return toMembershipResponse(membership);
    }

    @Transactional
    public void deleteMembership(UUID userId, UUID organizationId, UUID membershipId) {
        requireOwner(userId, organizationId);

        Membership membership = membershipRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(membershipId, organizationId)
                .orElseThrow(() -> new MembershipNotFoundException(membershipId));

        if (membership.getRole().equals(MembershipRole.OWNER) && membershipRepository.countByOrganization_IdAndRoleAndDeletedAtIsNull(organizationId, MembershipRole.OWNER) == 1) {
            throw new MembershipForbiddenException("You cannot delete the sole owner of this organization.");
        }

        membership.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));

    }

    @Transactional(readOnly = true)
    public Membership requireOwner(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException());

        if (membership.getRole() != MembershipRole.OWNER) {
            throw new MembershipForbiddenException("You do not have permission to access this organization.");
        }

        return membership;
    }

    @Transactional(readOnly = true)
    public Membership requireOwnerOrAdmin(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException());

        if (membership.getRole() != MembershipRole.OWNER && membership.getRole() != MembershipRole.ADMIN) {
            throw new MembershipForbiddenException("You do not have permission to access this organization.");
        }

        return membership;
    }

    @Transactional(readOnly = true)
    public Membership requireMember(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException());

        // active membership is enough. A member can be OWNER, ADMIN or MEMBER
        return membership;
    }

    private MembershipResponse toMembershipResponse(Membership membership) {
        return new MembershipResponse(
                membership.getId(),
                membership.getUser().getId(),
                membership.getUser().getName(),
                membership.getUser().getEmail(),
                membership.getRole(),
                membership.getCreatedAt()
        );
    }

}
