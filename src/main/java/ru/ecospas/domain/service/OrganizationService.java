package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.OrganizationAddressRepository;
import ru.ecospas.domain.repository.OrganizationContactRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.repository.OrganizationSignerRepository;
import ru.ecospas.web.dto.request.organization.SaveOrganizationRequest;
import ru.ecospas.web.helper.OrganizationSaveHelper;
import ru.ecospas.web.mapper.organization.OrganizationRequestMapper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationRequestMapper organizationRequestMapper;
    private final CurrentUserService currentUserService;
    private final SecurityService securityService;

    private final OrganizationAddressRepository addressRepository;
    private final OrganizationSignerRepository signerRepository;
    private final OrganizationContactRepository contactRepository;

    private final OrganizationSaveHelper helper;

    public Organization load(Integer id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public Organization create(User user) {
        Organization organization = new Organization();
        organization.setUser(user);
        return organization;
    }

    public List<Organization> findAll() {
        User user = currentUserService.currentUser();
        return organizationRepository.findByUserId(user.getId());
    }

    public Organization saveOrganization(HttpServletRequest req, Organization organization) {
        helper.mapOrganization(req, organization);
        return organizationRepository.save(organization);
    }

    private void saveAddress(HttpServletRequest req, Organization organization) {
        OrganizationAddress address = addressRepository
                .findByOrganizationId(organization.getId())
                .orElseGet(OrganizationAddress::new);
        helper.mapAddress(req, address);
        address.setOrganization(organization);
        addressRepository.save(address);
    }

    private void saveSigner(HttpServletRequest req, Organization organization) {
        OrganizationSigner signer = signerRepository
                .findByOrganizationId(organization.getId())
                .orElseGet(OrganizationSigner::new);
        helper.mapSigner(req, signer);
        signer.setOrganization(organization);
        signerRepository.save(signer);
    }

    private void saveContacts(HttpServletRequest req, Organization organization) {
        List<OrganizationContact> contacts = helper.mapContacts(req);
        List<OrganizationContact> oldContacts =
                contactRepository.findAllByOrganizationId(organization.getId());
        SyncListUtils.syncList(
                contacts,
                oldContacts,
                OrganizationContact::getId,
                contactRepository::deleteById
        );
        for (OrganizationContact contact : contacts) {
            contact.setOrganization(organization);
            contactRepository.save(contact);
        }
    }

    public Organization save(HttpServletRequest req, Organization organization) {
        organization = saveOrganization(req, organization);
        saveAddress(req, organization);
        saveSigner(req, organization);
        saveContacts(req, organization);
        return organization;
    }

    //REST
    public Organization create(SaveOrganizationRequest request) {
        Organization organization = new Organization();
        organization.setUser(currentUserService.requireCurrentUser());
        return save(request, organization);
    }

    public Organization save(SaveOrganizationRequest request, Organization organization) {
        organizationRequestMapper.toOrganization(request, organization);
        saveAddress(request, organization);
        saveSigner(request, organization);
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

    private void saveSigner(SaveOrganizationRequest request, Organization organization) {
        organization.getSigners().clear();
        List<OrganizationSigner> signers = organizationRequestMapper.toSigners(request.signer());
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
}