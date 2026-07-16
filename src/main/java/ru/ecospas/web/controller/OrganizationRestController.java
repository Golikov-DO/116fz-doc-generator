package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.service.OrganizationService;
import ru.ecospas.web.dto.request.organization.SaveOrganizationRequest;
import ru.ecospas.web.dto.response.organization.OrganizationResponse;
import ru.ecospas.web.mapper.organization.OrganizationResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationRestController {

    private final OrganizationService organizationService;
    private final OrganizationResponseMapper responseMapper;

    @GetMapping
    public List<OrganizationResponse> getOrganizations() {
        return responseMapper.toResponses(
                organizationService.findAll()
        );
    }

    @PostMapping
    public OrganizationResponse createOrganization(
            @Valid
            @RequestBody
            SaveOrganizationRequest request
    ) {
        Organization organization = organizationService.create(request);
        return responseMapper.toResponse(organization);
    }

    @PutMapping("/{id}")
    public OrganizationResponse updateOrganization(
            @PathVariable Integer id,
            @Valid
            @RequestBody SaveOrganizationRequest request
    ) {
        Organization organization = organizationService.update(id, request);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        return responseMapper.toResponse(organization);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable Integer id) {
        organizationService.delete(id);
    }

    @GetMapping("/{id}")
    public OrganizationResponse getOrganization(@PathVariable Integer id) {
        Organization organization = organizationService.load(id);

        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }

        return responseMapper.toResponse(organization);
    }
}