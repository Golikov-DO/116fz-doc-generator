package ru.ecospas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.config.AbstractIntegrationTest;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ObjectModelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectModelRepository objectRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveObject() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        assertNotNull(object.getId());
    }

    @Test
    void shouldFindObjectById() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        ObjectModel found = objectRepository.findById(object.getId())
                .orElseThrow();

        assertEquals(object.getId(), found.getId());
        assertEquals(object.getObjectFullName(), found.getObjectFullName());
    }

    @Test
    void shouldUpdateObject() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        object.setObjectFullName("Updated object");

        objectRepository.save(object);

        ObjectModel updated = objectRepository.findById(object.getId())
                .orElseThrow();

        assertEquals("Updated object", updated.getObjectFullName());
    }

    @Test
    void shouldDeleteObject() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        Integer id = object.getId();

        objectRepository.delete(object);

        assertTrue(objectRepository.findById(id).isEmpty());
    }

    @Test
    void shouldFindObjectsByOrganizationId() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        List<ObjectModel> result =
                objectRepository.findByOrganizationId(organization.getId());

        assertEquals(1, result.size());
        assertEquals(object.getId(), result.getFirst().getId());
    }

    @Test
    void shouldFindObjectByIdAndOrganizationId() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        Optional<ObjectModel> found =
                objectRepository.findByIdAndOrganizationId(
                        object.getId(),
                        organization.getId());

        assertTrue(found.isPresent());
        assertEquals(object.getId(), found.get().getId());
    }

    @Test
    void shouldFindObjectByIdAndOrganizationUserId() {

        User user = createUser();
        Organization organization = organizationRepository.save(createOrganization(user));

        ObjectModel object = objectRepository.save(createObject(organization));

        Optional<ObjectModel> found =
                objectRepository.findByIdAndOrganizationUserId(
                        object.getId(),
                        user.getId());

        assertTrue(found.isPresent());
        assertEquals(object.getId(), found.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenObjectBelongsToAnotherOrganization() {

        User user = createUser();

        Organization organization1 =
                organizationRepository.save(createOrganization(user));

        Organization organization2 =
                organizationRepository.save(createOrganization(user));

        ObjectModel object =
                objectRepository.save(createObject(organization1));

        assertTrue(
                objectRepository.findByIdAndOrganizationId(
                        object.getId(),
                        organization2.getId())
                        .isEmpty()
        );
    }

    @Test
    void shouldReturnEmptyWhenObjectBelongsToAnotherUser() {

        User owner = createUser();
        User anotherUser = createUser();

        Organization organization =
                organizationRepository.save(createOrganization(owner));

        ObjectModel object =
                objectRepository.save(createObject(organization));

        assertTrue(
                objectRepository.findByIdAndOrganizationUserId(
                        object.getId(),
                        anotherUser.getId())
                        .isEmpty()
        );
    }

    private User createUser() {

        String suffix = UUID.randomUUID()
                .toString()
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

    private ObjectModel createObject(Organization organization) {

        ObjectModel object = new ObjectModel();

        object.setOrganization(organization);
        object.setObjectFullName("Test object");
        object.setHazardClass(1);
        object.setEmergencyCommission(true);

        return object;
    }
}