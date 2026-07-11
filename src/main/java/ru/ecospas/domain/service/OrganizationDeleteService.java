package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.repository.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationDeleteService {

    private final OrganizationRepository organizationRepository;

    private final ObjectModelRepository objectRepository;
    private final ObjectDeleteService objectDeleteService;

    private final OrganizationAddressRepository organizationAddressRepository;
    private final OrganizationSignerRepository organizationSignerRepository;
    private final OrganizationContactRepository organizationContactRepository;

    public void delete(int orgId) {

        objectRepository.findByOrganizationId(orgId)
                .forEach(object -> objectDeleteService.delete(object.getId()));

        organizationContactRepository.deleteAllByOrganizationId(orgId);

        organizationAddressRepository.findByOrganizationId(orgId)
                .ifPresent(organizationAddressRepository::delete);

        organizationSignerRepository.deleteAllByOrganizationId(orgId);

        organizationRepository.deleteById(orgId);
    }
}