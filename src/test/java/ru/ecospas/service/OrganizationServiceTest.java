package ru.ecospas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.OrganizationService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.dto.request.organization.SaveOrganizationRequest;
import ru.ecospas.web.mapper.organization.OrganizationRequestMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    private SaveOrganizationRequest createRequest() {

        return new SaveOrganizationRequest(
                "Organization",
                "ORG",
                "Testing",
                true,
                null,
                List.of(),
                List.of()
        );
    }

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationRequestMapper organizationRequestMapper;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private OrganizationService organizationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setRole(Role.USER);
    }

    @Test
    void shouldLoadOrganization() {

        Organization organization = new Organization();

        when(organizationRepository.findById(1))
                .thenReturn(Optional.of(organization));

        Organization result = organizationService.load(1);

        assertEquals(organization, result);
    }

    @Test
    void shouldReturnNullWhenOrganizationNotFound() {

        when(organizationRepository.findById(1))
                .thenReturn(Optional.empty());

        assertNull(organizationService.load(1));
    }

    @Test
    void shouldAssignCurrentUserOnCreate() {

        SaveOrganizationRequest request = createRequest();

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(organizationRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        organizationService.create(request);

        ArgumentCaptor<Organization> captor =
                ArgumentCaptor.forClass(Organization.class);

        verify(organizationRepository).save(captor.capture());

        assertEquals(user, captor.getValue().getUser());

        verify(organizationRequestMapper)
                .toOrganization(request, captor.getValue());
    }

    @Test
    void shouldReturnNullWhenUpdatingMissingOrganization() {

        SaveOrganizationRequest request = mock(SaveOrganizationRequest.class);

        when(organizationRepository.findById(100))
                .thenReturn(Optional.empty());

        assertNull(organizationService.update(100, request));

        verify(organizationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithoutAccess() {

        SaveOrganizationRequest request = mock(SaveOrganizationRequest.class);

        Organization organization = new Organization();

        when(organizationRepository.findById(1))
                .thenReturn(Optional.of(organization));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(securityService.hasAccess(user, organization))
                .thenReturn(false);

        assertThrows(SecurityException.class,
                () -> organizationService.update(1, request));

        verify(organizationRepository, never()).save(any());
    }

    @Test
    void shouldUpdateOrganization() {

        SaveOrganizationRequest request = createRequest();

        Organization organization = new Organization();

        when(organizationRepository.findById(1))
                .thenReturn(Optional.of(organization));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(securityService.hasAccess(user, organization))
                .thenReturn(true);

        when(organizationRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Organization result = organizationService.update(1, request);

        assertNotNull(result);

        verify(organizationRequestMapper)
                .toOrganization(request, organization);

        verify(organizationRepository)
                .save(organization);
    }

    @Test
    void shouldDeleteOrganization() {

        Organization organization = new Organization();

        when(organizationRepository.findById(1))
                .thenReturn(Optional.of(organization));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(securityService.hasAccess(user, organization))
                .thenReturn(true);

        organizationService.delete(1);

        verify(organizationRepository).delete(organization);
    }

    @Test
    void shouldDoNothingWhenDeletingMissingOrganization() {

        when(organizationRepository.findById(1))
                .thenReturn(Optional.empty());

        organizationService.delete(1);

        verify(organizationRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingWithoutAccess() {

        Organization organization = new Organization();

        when(organizationRepository.findById(1))
                .thenReturn(Optional.of(organization));

        when(currentUserService.requireCurrentUser())
                .thenReturn(user);

        when(securityService.hasAccess(user, organization))
                .thenReturn(false);

        assertThrows(SecurityException.class,
                () -> organizationService.delete(1));

        verify(organizationRepository, never()).delete(any());
    }

    @Test
    void shouldReturnAllOrganizationsForAdmin() {

        User admin = new User();
        admin.setRole(Role.ADMIN);

        List<Organization> organizations =
                List.of(new Organization(), new Organization());

        when(currentUserService.isAdmin()).thenReturn(true);
        when(organizationRepository.findAll()).thenReturn(organizations);

        List<Organization> result = organizationService.findAll();

        assertEquals(2, result.size());

        verify(organizationRepository).findAll();
    }

    @Test
    void shouldReturnCurrentUserOrganizations() {

        List<Organization> organizations =
                List.of(new Organization());

        when(currentUserService.isAdmin()).thenReturn(false);
        when(currentUserService.currentUser()).thenReturn(user);

        when(organizationRepository
                .findByUserIdOrderByOrganizationShortNameAsc(1))
                .thenReturn(organizations);

        List<Organization> result = organizationService.findAll();

        assertEquals(1, result.size());

        verify(organizationRepository)
                .findByUserIdOrderByOrganizationShortNameAsc(1);
    }
}