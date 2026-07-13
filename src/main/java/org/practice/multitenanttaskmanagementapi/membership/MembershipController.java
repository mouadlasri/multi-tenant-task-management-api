package org.practice.multitenanttaskmanagementapi.membership;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.membership.dto.CreateMembershipRequest;
import org.practice.multitenanttaskmanagementapi.membership.dto.MembershipResponse;
import org.practice.multitenanttaskmanagementapi.membership.dto.UpdateMembershipRequest;
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
@RequestMapping("/api/v1/organizations/{organizationId}/members")
public class MembershipController {
    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping
    public ResponseEntity<Page<MembershipResponse>> getAllMembersByOrganizationId(
            Authentication authentication,
            @PathVariable UUID organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        User user = (User) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MembershipResponse> membershipResponsesPage = membershipService.getAllMembersByOrganizationId(user.getId(), organizationId, pageable);

        return ResponseEntity.ok(membershipResponsesPage);
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> getMemberByOrganizationId(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID membershipId) {
        User user = (User) authentication.getPrincipal();

        MembershipResponse membershipResponse = membershipService.getMemberByOrganizationId(user.getId(), membershipId, organizationId);

        return ResponseEntity.ok(membershipResponse);
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(Authentication authentication, @PathVariable UUID organizationId, @Valid @RequestBody CreateMembershipRequest createMembershipRequest) {
        User user = (User) authentication.getPrincipal();

        MembershipResponse membershipResponse = membershipService.createMembership(user.getId(), organizationId, createMembershipRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
    }

    @PatchMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> updateMembership(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID membershipId, @Valid @RequestBody UpdateMembershipRequest updateMembershipRequest) {
        User user = (User) authentication.getPrincipal();

        MembershipResponse membershipResponse = membershipService.updateMembership(user.getId(), organizationId, membershipId, updateMembershipRequest);

        return ResponseEntity.ok(membershipResponse);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<Void> deleteMembership(Authentication authentication, @PathVariable UUID organizationId, @PathVariable UUID membershipId) {
        User user = (User) authentication.getPrincipal();

        membershipService.deleteMembership(user.getId(), organizationId, membershipId);

        return ResponseEntity.noContent().build();
    }
}
