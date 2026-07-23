package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.web.dto.request.object.ObjectImageRequest;
import ru.ecospas.web.dto.request.object.SaveObjectRequest;
import ru.ecospas.web.dto.response.object.ObjectWithOrgResponse;
import ru.ecospas.web.mapper.object.ObjectRequestMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectService {

    private final ObjectModelRepository objectRepository;
    private final OrganizationRepository organizationRepository;
    private final ObjectRequestMapper objectRequestMapper;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public ObjectModel load(Integer organizationId, Integer id) {
        ObjectModel object = objectRepository
                .findByIdAndOrganizationId(id, organizationId)
                .orElse(null);

        if (object == null) {
            return null;
        }

        // Object collections
        Hibernate.initialize(object.getCompositionKchs());
        Hibernate.initialize(object.getResponsiblePersons());
        Hibernate.initialize(object.getFireEquipments());
        Hibernate.initialize(object.getTechnologicalEquipments());
        Hibernate.initialize(object.getTechnologicalBlocks());
        Hibernate.initialize(object.getStructures());
        Hibernate.initialize(object.getImages());

        // Organization and its data
        if (object.getOrganization() != null) {
            Hibernate.initialize(object.getOrganization());
            Hibernate.initialize(object.getOrganization().getAddress());
            Hibernate.initialize(object.getOrganization().getSigners());
            Hibernate.initialize(object.getOrganization().getContacts());
        }

        // ASF and its data
        if (object.getAsf() != null) {
            Hibernate.initialize(object.getAsf());
            Hibernate.initialize(object.getAsf().getCertificate());
            Hibernate.initialize(object.getAsf().getPersonnel());
            Hibernate.initialize(object.getAsf().getSpecialists());
            Hibernate.initialize(object.getAsf().getDeployment());
            Hibernate.initialize(object.getAsf().getSigners());
            Hibernate.initialize(object.getAsf().getWorkTypes());
            Hibernate.initialize(object.getAsf().getImages());
        }

        // Related entities
        if (object.getAddress() != null) Hibernate.initialize(object.getAddress());
        if (object.getInsurancePolicy() != null) Hibernate.initialize(object.getInsurancePolicy());
        if (object.getMinimumBalance() != null) Hibernate.initialize(object.getMinimumBalance());
        if (object.getType() != null) Hibernate.initialize(object.getType());
        if (object.getCity() != null) Hibernate.initialize(object.getCity());
        if (object.getHazardousSubstance() != null) Hibernate.initialize(object.getHazardousSubstance());

        return object;
    }

    public ObjectModel create(Integer organizationId, SaveObjectRequest request) {
        ObjectModel object = new ObjectModel();
        object.setOrganization(organizationRepository.getReferenceById(organizationId));
        return save(request, object);
    }

    public ObjectModel save(SaveObjectRequest request, ObjectModel object) {
        objectRequestMapper.toObject(request, object);
        saveAddress(request, object);
        saveInsurancePolicy(request, object);
        saveMinimumBalance(request, object);
        saveCompositionKchs(request, object);
        saveResponsiblePersons(request, object);
        saveFireEquipments(request, object);
        saveTechnologicalEquipments(request, object);
        saveTechnologicalBlocks(request, object);
        saveStructures(request, object);
        saveImages(request, object);
        return objectRepository.save(object);
    }

    private void saveAddress(SaveObjectRequest request, ObjectModel object) {
        ObjectAddress address = object.getAddress();
        if (address == null) {
            address = new ObjectAddress();
        }
        objectRequestMapper.toAddress(request.address(),address);
        address.setObject(object);
        object.setAddress(address);
    }

    private void saveInsurancePolicy(SaveObjectRequest request, ObjectModel object) {
        ObjectInsurancePolicy policy = object.getInsurancePolicy();
        if (policy == null) {
            policy = new ObjectInsurancePolicy();
        }
        objectRequestMapper.toInsurancePolicy(request.insurancePolicy(), policy);
        policy.setObject(object);
        object.setInsurancePolicy(policy);
    }

    private void saveMinimumBalance(SaveObjectRequest request, ObjectModel object) {
        ObjectOrderMinimumBalance balance = object.getMinimumBalance();
        if (balance == null) {
            balance = new ObjectOrderMinimumBalance();
        }
        objectRequestMapper.toMinimumBalance(request.minimumBalance(), balance);
        balance.setObject(object);
        object.setMinimumBalance(balance);
    }

    private void saveCompositionKchs(SaveObjectRequest request, ObjectModel object){
        object.getCompositionKchs().clear();
        List<ObjectCompositionKchs> list =
                objectRequestMapper.toCompositionKchs(request.compositionKchs());
        for (ObjectCompositionKchs item : list) {
            item.setObject(object);
        }
        object.getCompositionKchs().addAll(list);
    }

    private void saveResponsiblePersons(SaveObjectRequest request, ObjectModel object) {
        object.getResponsiblePersons().clear();
        List<ObjectPersonsResponsible> list =
                objectRequestMapper.toResponsiblePersons(request.responsiblePersons());
        for (ObjectPersonsResponsible item : list) {
            item.setObject(object);
        }
        object.getResponsiblePersons().addAll(list);
    }

    private void saveFireEquipments(SaveObjectRequest request, ObjectModel object) {
        object.getFireEquipments().clear();
        List<ObjectFireEquipment> list =
                objectRequestMapper.toFireEquipments(request.fireEquipments());
        for (ObjectFireEquipment item : list) {
            item.setObject(object);
        }
        object.getFireEquipments().addAll(list);
    }

    private void saveTechnologicalEquipments(SaveObjectRequest request, ObjectModel object) {
        object.getTechnologicalEquipments().clear();
        List<ObjectTechnologicalEquipment> list =
                objectRequestMapper.toTechnologicalEquipments(
                        request.technologicalEquipments()
                );
        for (ObjectTechnologicalEquipment item : list) {
            item.setObject(object);
        }
        object.getTechnologicalEquipments().addAll(list);
    }

    private void saveTechnologicalBlocks(SaveObjectRequest request, ObjectModel object) {
        object.getTechnologicalBlocks().clear();
        List<ObjectTechnologicalBlock> list =
                objectRequestMapper.toTechnologicalBlocks(request.technologicalBlocks());
        for (ObjectTechnologicalBlock item : list) {
            item.setObject(object);
        }
        object.getTechnologicalBlocks().addAll(list);
    }

    private void saveStructures(SaveObjectRequest request, ObjectModel object) {
        object.getStructures().clear();
        List<ObjectStructure> list = objectRequestMapper.toStructures(request.structures());
        for (ObjectStructure item : list) {
            item.setObject(object);
        }
        object.getStructures().addAll(list);
    }

    private void saveImages(SaveObjectRequest request, ObjectModel object) {
        if (request.images() == null) {
            object.getImages().clear();
            return;
        }
        Map<Integer, ObjectImage> existing = object.getImages().stream()
                .filter(image -> image.getId() != null)
                .collect(Collectors.toMap(
                        ObjectImage::getId,
                        Function.identity()
                ));
        for (ObjectImageRequest dto : request.images()) {
            if (dto.id() == null) {
                continue;
            }
            ObjectImage image = existing.get(dto.id());
            if (image == null) {
                continue;
            }
            image.setCaption(dto.caption());
            image.setLinkText(dto.linkText());
            image.setGroupKey(dto.groupKey());
        }
    }

    public List<ObjectModel> findAll(Integer organizationId) {
        return objectRepository.findByOrganizationId(organizationId);
    }

    public ObjectModel update(Integer organizationId, Integer id, SaveObjectRequest request) {
        ObjectModel object = load(organizationId, id);
        if (object == null) {
            return null;
        }
        return save(request, object);
    }

    public void delete(Integer organizationId, Integer id) {
        ObjectModel object = load(organizationId, id);
        if (object != null) {
            objectRepository.delete(object);
        }
    }

    public ObjectModel create(SaveObjectRequest request) {
        ObjectModel object = new ObjectModel();
        object.setOrganization(
                organizationRepository.getReferenceById(request.organizationId())
        );
        return save(request, object);
    }

    @Transactional(readOnly = true)
    public List<ObjectWithOrgResponse> findAllObjectsWithOrg() {
        User currentUser = currentUserService.requireCurrentUser();
        List<Organization> organizations;

        if (currentUserService.isAdmin()) {
            organizations = organizationRepository.findAll();
        } else {
            organizations = organizationRepository.findByUserIdOrderByOrganizationShortNameAsc(currentUser.getId());
        }

        List<ObjectWithOrgResponse> result = new ArrayList<>();
        for (Organization org : organizations) {
            for (ObjectModel obj : org.getObjects()) {
                result.add(new ObjectWithOrgResponse(
                        obj.getId(),
                        obj.getObjectFullName(),
                        org.getId(),
                        org.getOrganizationShortName()
                ));
            }
        }
        return result;
    }
}