package org.practice.multitenanttaskmanagementapi.organization;

import jakarta.validation.Valid;
import org.practice.multitenanttaskmanagementapi.organization.dto.CreateOrganizationRequest;
import org.practice.multitenanttaskmanagementapi.organization.dto.OrganizationResponse;
import org.practice.multitenanttaskmanagementapi.organization.dto.UpdateOrganizationRequest;
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
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<Page<OrganizationResponse>> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OrganizationResponse> organizationResponsePage = organizationService.getAllOrganizations(pageable);

        return ResponseEntity.ok(organizationResponsePage);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable UUID organizationId) {
        OrganizationResponse organizationResponse = organizationService.getOrganizationById(organizationId);

        return ResponseEntity.ok(organizationResponse);
    }

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(Authentication authentication, @Valid @RequestBody CreateOrganizationRequest createOrganizationRequest) {
        User user = (User) authentication.getPrincipal();

        OrganizationResponse organizationResponse = organizationService.createOrganization(user.getId(), createOrganizationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(organizationResponse);
    }

    @PatchMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> updateOrganization(Authentication authentication, @PathVariable UUID organizationId, @Valid @RequestBody UpdateOrganizationRequest updateOrganizationRequest) {
        User user = (User) authentication.getPrincipal();

        OrganizationResponse organizationResponse = organizationService.updateOrganization(user.getId(), organizationId, updateOrganizationRequest);

        return ResponseEntity.ok(organizationResponse);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(Authentication authentication, @PathVariable UUID organizationId) {
        User user = (User) authentication.getPrincipal();

        organizationService.deleteOrganization(user.getId(), organizationId);

        return ResponseEntity.noContent().build();
    }



}
