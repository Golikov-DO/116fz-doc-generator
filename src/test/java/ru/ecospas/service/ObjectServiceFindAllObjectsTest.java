package ru.ecospas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ObjectService;
import ru.ecospas.web.dto.response.object.ObjectWithOrgResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectServiceFindAllObjectsTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ObjectService objectService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
    }

    @Test
    void shouldReturnAllObjectsForAdmin() {

        Organization organization = new Organization();
        organization.setId(10);
        organization.setOrganizationShortName("ORG");

        ObjectModel object = new ObjectModel();
        object.setId(20);
        object.setObjectFullName("Object");

        organization.setObjects(List.of(object));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(currentUserService.isAdmin())
                .thenReturn(true);

        when(organizationRepository.findAll())
                .thenReturn(List.of(organization));

        List<ObjectWithOrgResponse> result =
                objectService.findAllObjectsWithOrg();

        assertEquals(1, result.size());

        ObjectWithOrgResponse dto = result.getFirst();

        assertEquals(20, dto.id());
        assertEquals("Object", dto.objectFullName());
        assertEquals(10, dto.organizationId());
        assertEquals("ORG", dto.organizationShortName());

        verify(organizationRepository).findAll();
    }

    @Test
    void shouldReturnCurrentUserObjects() {

        Organization organization = new Organization();
        organization.setId(10);
        organization.setOrganizationShortName("ORG");

        ObjectModel object = new ObjectModel();
        object.setId(20);
        object.setObjectFullName("Object");

        organization.setObjects(List.of(object));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(currentUserService.isAdmin())
                .thenReturn(false);

        when(organizationRepository.findByUserIdOrderByOrganizationShortNameAsc(1))
                .thenReturn(List.of(organization));

        List<ObjectWithOrgResponse> result =
                objectService.findAllObjectsWithOrg();

        assertEquals(1, result.size());

        verify(organizationRepository)
                .findByUserIdOrderByOrganizationShortNameAsc(1);
    }

    @Test
    void shouldReturnEmptyList() {

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(currentUserService.isAdmin())
                .thenReturn(true);

        when(organizationRepository.findAll())
                .thenReturn(List.of());

        List<ObjectWithOrgResponse> result =
                objectService.findAllObjectsWithOrg();

        assertTrue(result.isEmpty());
    }
}