package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.OrganizationAddress;
import ru.ecospas.domain.model.OrganizationContact;
import ru.ecospas.domain.model.OrganizationSigner;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationAddressRepository;
import ru.ecospas.domain.repository.OrganizationContactRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.repository.OrganizationSignerRepository;
import ru.ecospas.web.helper.OrganizationSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

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

    public Organization saveOrganization(
            HttpServletRequest req,
            Organization organization
    ) {

        helper.mapOrganization(req, organization);

        return organizationRepository.save(organization);
    }

    private void saveAddress(
            HttpServletRequest req,
            Organization organization
    ) {

        OrganizationAddress address = addressRepository
                .findByOrganizationId(organization.getId())
                .orElseGet(OrganizationAddress::new);

        helper.mapAddress(req, address);

        address.setOrganization(organization);

        addressRepository.save(address);
    }

    private void saveSigner(
            HttpServletRequest req,
            Organization organization
    ) {

        OrganizationSigner signer = signerRepository
                .findByOrganizationId(organization.getId())
                .orElseGet(OrganizationSigner::new);

        helper.mapSigner(req, signer);

        signer.setOrganization(organization);

        signerRepository.save(signer);
    }

    private void saveContacts(
            HttpServletRequest req,
            Organization organization
    ) {

        List<OrganizationContact> contacts =
                helper.mapContacts(req);

        List<OrganizationContact> oldContacts =
                contactRepository.findAllByOrganizationId(
                        organization.getId()
                );

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

    public Organization save(
            HttpServletRequest req,
            Organization organization
    ) {

        organization = saveOrganization(req, organization);

        saveAddress(req, organization);

        saveSigner(req, organization);

        saveContacts(req, organization);

        return organization;
    }
}