package org.practice.multitenanttaskmanagementapi.membership;

import org.practice.multitenanttaskmanagementapi.membership.exception.MemberNotFoundException;
import org.practice.multitenanttaskmanagementapi.membership.exception.MembershipForbiddenException;
import org.practice.multitenanttaskmanagementapi.organization.Organization;
import org.practice.multitenanttaskmanagementapi.organization.dto.OrganizationResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAllMembersByOrganizationId(UUID organizationId) {
        // check role
//        Membership membership = membershipService.requireOwnerOrAdmin(userId, organizationId);

//        Page<Organization> organizationMembersPage = membershipRepository.find
    }

    public Membership requireOwner(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        if (membership.getRole() != MembershipRole.OWNER) {
            throw new MembershipForbiddenException();
        }

        return membership;
    }

    public Membership requireOwnerOrAdmin(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        if (membership.getRole() != MembershipRole.OWNER && membership.getRole() != MembershipRole.ADMIN) {
            throw new MembershipForbiddenException();
        }

        return membership;
    }

    public Membership requireMember(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        // active membership is enough. A member can be OWNER, ADMIN or MEMBER
        return membership;
    }

}
