package ru.ecospas.web.mapper.organization;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.web.dto.response.organization.OrganizationResponse;

import java.util.List;

@Component
public class OrganizationResponseMapper {

    public OrganizationResponse toResponse(Organization organization) {
        if (organization == null) {
            return null;
        }
        return new OrganizationResponse(
                organization.getId(),
                organization.getOrganizationName(),
                organization.getOrganizationShortName(),
                organization.getOrganizationTypeActivity(),
                organization.isOneTerritory()
        );
    }

    public List<OrganizationResponse> toResponses(List<Organization> organizations) {
        return organizations.stream()
                .map(this::toResponse)
                .toList();
    }
}