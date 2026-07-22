package ru.ecospas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.config.AbstractIntegrationTest;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class OrganizationRepositoryTest extends AbstractIntegrationTest {

    private User createUser() {
        String suffix = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8);

        User user = new User();
        user.setLogin("user" + suffix);
        user.setEmail("user" + suffix + "@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    private Organization createOrganization(User user) {
        Organization organization = new Organization();
        organization.setOrganizationName("Organization");
        organization.setOrganizationShortName("ORG");
        organization.setOrganizationTypeActivity("Testing");
        organization.setOneTerritory(true);
        organization.setUser(user);

        return organization;
    }

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveOrganization() {

        User user = createUser();

        Organization organization = createOrganization(user);

        Organization saved = organizationRepository.save(organization);

        assertNotNull(saved.getId());
        assertEquals("Organization", saved.getOrganizationName());
        assertEquals("ORG", saved.getOrganizationShortName());
        assertEquals(user.getId(), saved.getUser().getId());
    }

    @Test
    void shouldFindOrganizationById() {

        User user = createUser();

        Organization organization = createOrganization(user);
        organization = organizationRepository.save(organization);

        Organization found = organizationRepository.findById(organization.getId())
                .orElseThrow();

        assertEquals(organization.getId(), found.getId());
        assertEquals("Organization", found.getOrganizationName());
    }

    @Test
    void shouldUpdateOrganization() {

        User user = createUser();

        Organization organization = createOrganization(user);
        organization = organizationRepository.save(organization);

        organization.setOrganizationName("Updated");

        organizationRepository.save(organization);

        Organization updated = organizationRepository.findById(organization.getId())
                .orElseThrow();

        assertEquals("Updated", updated.getOrganizationName());
    }

    @Test
    void shouldDeleteOrganization() {

        User user = createUser();

        Organization organization = createOrganization(user);
        organization = organizationRepository.save(organization);

        Integer id = organization.getId();

        organizationRepository.delete(organization);

        assertFalse(organizationRepository.findById(id).isPresent());
    }

    @Test
    void shouldFindOrganizationsByUserId() {

        User user = createUser();

        organizationRepository.save(createOrganization(user));
        organizationRepository.save(createOrganization(user));

        assertEquals(2,
                organizationRepository.findByUserId(user.getId()).size());
    }

    @Test
    void shouldFindOrganizationsByUserIdOrderedByShortName() {

        User user = createUser();

        Organization b = createOrganization(user);
        b.setOrganizationShortName("BBB");

        Organization a = createOrganization(user);
        a.setOrganizationShortName("AAA");

        organizationRepository.save(b);
        organizationRepository.save(a);

        List<Organization> organizations =
                organizationRepository.findByUserIdOrderByOrganizationShortNameAsc(user.getId());

        assertEquals("AAA", organizations.get(0).getOrganizationShortName());
        assertEquals("BBB", organizations.get(1).getOrganizationShortName());
    }
}