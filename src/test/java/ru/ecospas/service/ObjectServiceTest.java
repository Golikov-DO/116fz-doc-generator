package ru.ecospas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.ObjectService;
import ru.ecospas.web.dto.request.object.SaveObjectRequest;
import ru.ecospas.web.mapper.object.ObjectRequestMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectServiceTest {

    private SaveObjectRequest createRequest() {

        return new SaveObjectRequest(
                1,          // organizationId
                null,       // asfId
                null,       // cityId
                null,       // typeId
                null,       // hazardousSubstanceId

                null,       // asfSignerId
                1,          // hazardClass

                "Test object",
                null,
                null,
                null,
                true,
                null,

                null,       // address
                null,       // insurancePolicy
                null,       // minimumBalance

                List.of(),  // compositionKchs
                List.of(),  // responsiblePersons
                List.of(),  // fireEquipments
                List.of(),  // technologicalEquipments
                List.of(),  // technologicalBlocks
                List.of(),  // structures
                List.of()   // images
        );
    }

    @Mock
    private ObjectModelRepository objectRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private ObjectRequestMapper objectRequestMapper;

    @InjectMocks
    private ObjectService objectService;

    private Organization organization;

    @BeforeEach
    void setUp() {
        organization = new Organization();
        organization.setId(1);
    }

    @Test
    void shouldCreateObject() {

        SaveObjectRequest request = createRequest();

        when(organizationRepository.getReferenceById(1))
                .thenReturn(organization);

        when(objectRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        objectService.create(1, request);

        ArgumentCaptor<ObjectModel> captor =
                ArgumentCaptor.forClass(ObjectModel.class);

        verify(objectRepository).save(captor.capture());

        assertEquals(organization, captor.getValue().getOrganization());

        verify(objectRequestMapper)
                .toObject(request, captor.getValue());
    }

    @Test
    void shouldCreateObjectFromRequest() {

        SaveObjectRequest request = createRequest();

        when(organizationRepository.getReferenceById(1))
                .thenReturn(organization);

        when(objectRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        objectService.create(request);

        verify(objectRepository).save(any(ObjectModel.class));

        ArgumentCaptor<ObjectModel> captor =
                ArgumentCaptor.forClass(ObjectModel.class);

        verify(objectRepository).save(captor.capture());

        assertEquals(organization, captor.getValue().getOrganization());

        verify(objectRequestMapper)
                .toObject(request, captor.getValue());
    }

    @Test
    void shouldLoadObject() {

        ObjectModel object = new ObjectModel();

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.of(object));

        ObjectModel result = objectService.load(1, 10);

        assertEquals(object, result);
    }

    @Test
    void shouldReturnNullWhenObjectNotFound() {

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.empty());

        assertNull(objectService.load(1, 10));
    }

    @Test
    void shouldUpdateObject() {

        SaveObjectRequest request = createRequest();

        ObjectModel object = new ObjectModel();

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.of(object));

        when(objectRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        ObjectModel result =
                objectService.update(1, 10, request);

        assertNotNull(result);

        verify(objectRequestMapper)
                .toObject(request, object);

        verify(objectRepository).save(object);
    }

    @Test
    void shouldReturnNullWhenUpdatingMissingObject() {

        SaveObjectRequest request = createRequest();

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.empty());

        assertNull(objectService.update(1, 10, request));

        verify(objectRepository, never()).save(any());
    }

    @Test
    void shouldDeleteObject() {

        ObjectModel object = new ObjectModel();

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.of(object));

        objectService.delete(1, 10);

        verify(objectRepository).delete(object);
    }

    @Test
    void shouldIgnoreDeletingMissingObject() {

        when(objectRepository.findByIdAndOrganizationId(10, 1))
                .thenReturn(Optional.empty());

        objectService.delete(1, 10);

        verify(objectRepository, never()).delete(any());
    }

    @Test
    void shouldReturnObjectsByOrganization() {

        List<ObjectModel> objects =
                List.of(new ObjectModel(), new ObjectModel());

        when(objectRepository.findByOrganizationId(1))
                .thenReturn(objects);

        List<ObjectModel> result =
                objectService.findAll(1);

        assertEquals(objects, result);

        verify(objectRepository)
                .findByOrganizationId(1);
    }
}