package org.practice.multitenanttaskmanagementapi.membership;

import org.practice.multitenanttaskmanagementapi.membership.dto.MembershipResponse;
import org.practice.multitenanttaskmanagementapi.membership.exception.MemberNotFoundException;
import org.practice.multitenanttaskmanagementapi.membership.exception.MembershipForbiddenException;
import org.practice.multitenanttaskmanagementapi.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Membership requireOwner(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException());

        if (membership.getRole() != MembershipRole.OWNER) {
            throw new MembershipForbiddenException();
        }

        return membership;
    }

    @Transactional(readOnly = true)
    public Membership requireOwnerOrAdmin(UUID userId, UUID organizationId) {
        Membership membership = membershipRepository.findByOrganizationIdAndUserIdAndDeletedAtIsNull(organizationId, userId)
                .orElseThrow(() -> new MemberNotFoundException());

        if (membership.getRole() != MembershipRole.OWNER && membership.getRole() != MembershipRole.ADMIN) {
            throw new MembershipForbiddenException();
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
