package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.web.dto.request.organization.SaveOrganizationRequest;
import ru.ecospas.web.mapper.organization.OrganizationRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRequestMapper organizationRequestMapper;
    private final CurrentUserService currentUserService;
    private final SecurityService securityService;

    public Organization load(Integer id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public Organization create(SaveOrganizationRequest request) {
        Organization organization = new Organization();
        organization.setUser(currentUserService.requireCurrentUser());
        return save(request, organization);
    }

    public Organization save(SaveOrganizationRequest request, Organization organization) {
        organizationRequestMapper.toOrganization(request, organization);
        saveAddress(request, organization);
        saveSigners(request, organization);
        saveContacts(request, organization);
        return organizationRepository.save(organization);
    }

    public Organization update(Integer id, SaveOrganizationRequest request) {
        Organization organization = load(id);
        if (organization == null) {
            return null;
        }
        User currentUser = currentUserService.requireCurrentUser();
        if (!securityService.hasAccess(currentUser, organization)) {
            throw new SecurityException("Access denied");
        }
        return save(request, organization);
    }

    public void delete(Integer id) {
        Organization organization = load(id);
        if (organization == null) {
            return;
        }
        User currentUser = currentUserService.requireCurrentUser();
        if (!securityService.hasAccess(currentUser, organization)) {
            throw new SecurityException("Access denied");
        }
        organizationRepository.delete(organization);
    }

    private void saveContacts(SaveOrganizationRequest request, Organization organization) {
        organization.getContacts().clear();
        List<OrganizationContact> contacts = organizationRequestMapper.toContacts(request.contacts());
        for (OrganizationContact contact : contacts) {
            contact.setOrganization(organization);
        }
        organization.getContacts().addAll(contacts);
    }

    private void saveSigners(SaveOrganizationRequest request, Organization organization) {
        organization.getSigners().clear();
        List<OrganizationSigner> signers = organizationRequestMapper.toSigners(request.signers());
        for (OrganizationSigner signer : signers) {
            signer.setOrganization(organization);
        }
        organization.getSigners().addAll(signers);
    }

    private void saveAddress(SaveOrganizationRequest request, Organization organization) {
        OrganizationAddress address = organization.getAddress();
        if (address == null) {
            address = new OrganizationAddress();
        }
        organizationRequestMapper.toAddress(request.address(), address);
        address.setOrganization(organization);
        organization.setAddress(address);
    }

    public List<Organization> findAll() {
        if (currentUserService.isAdmin()) {
            return organizationRepository.findAll();
        }
        User user = currentUserService.currentUser();
        return organizationRepository.findByUserIdOrderByOrganizationShortNameAsc(user.getId());
    }
}