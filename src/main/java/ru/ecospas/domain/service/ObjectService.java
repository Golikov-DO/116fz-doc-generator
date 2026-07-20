package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;
import ru.ecospas.web.dto.request.object.ObjectImageRequest;
import ru.ecospas.web.dto.request.object.SaveObjectRequest;
import ru.ecospas.web.dto.response.object.ObjectWithOrgResponse;
import ru.ecospas.web.helper.ObjectSaveHelper;
import ru.ecospas.web.mapper.object.ObjectRequestMapper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectService {

    private final ObjectModelRepository objectRepository;

    private final ReferenceCityRepository cityRepository;
    private final AsfRepository asfRepository;
    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final ReferenceTypeRepository typeRepository;

    private final ObjectAddressRepository addressRepository;
    private final ObjectInsurancePolicyRepository policyRepository;
    private final ObjectOrderMinimumBalanceRepository balanceRepository;
    private final OrganizationRepository organizationRepository;

    private final ObjectCompositionKchsRepository kchsRepository;
    private final ObjectTechnologicalEquipmentRepository equipmentRepository;
    private final ObjectStructureRepository structureRepository;
    private final ObjectTechnologicalBlockRepository technoBlockRepository;
    private final ObjectFireEquipmentRepository fireEquipmentRepository;
    private final ObjectPersonsResponsibleRepository personsRepository;
    private final ObjectImageRepository imageRepository;

    private final ScenarioRepository scenarioRepository;
    private final ObjectScenarioRepository objectScenarioRepository;

    private final ObjectRequestMapper objectRequestMapper;
    private final CurrentUserService currentUserService;

    public ObjectModel load(Integer id) {
        return objectRepository
                .findByIdAndOrganizationUserId(
                        id,
                        currentUserService.requireCurrentUser().getId()
                )
                .orElse(null);
    }

    public ObjectModel create(Organization organization) {

        ObjectModel object = new ObjectModel();
        object.setOrganization(organization);

        return object;
    }

    public ObjectModel saveObject(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        helper.mapObject(req, object);

        int cityId = paramInt(req, "object_city_id", 0);
        if (cityId > 0) {
            object.setCity(
                    cityRepository.findById(cityId).orElse(null)
            );
        }

        int asfId = paramInt(req, "object_asf_id", 0);
        if (asfId > 0) {
            object.setAsf(
                    asfRepository.findById(asfId).orElse(null)
            );
        }

        int substanceId = paramInt(req, "hazardous_substance_id", 0);
        if (substanceId > 0) {
            object.setHazardousSubstance(
                    substanceRepository.findById(substanceId).orElse(null)
            );
        }

        int typeId = paramInt(req, "object_type_id", 0);
        if (typeId > 0) {
            object.setType(
                    typeRepository.findById(typeId).orElse(null)
            );
        }

        return objectRepository.save(object);
    }

    public void saveAddress(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        ObjectAddress address = addressRepository
                .findByObjectId(object.getId())
                .orElseGet(ObjectAddress::new);

        helper.mapAddress(req, address);

        address.setObject(object);

        addressRepository.save(address);
    }

    public void savePolicy(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        ObjectInsurancePolicy policy = policyRepository
                .findByObjectId(object.getId())
                .orElseGet(ObjectInsurancePolicy::new);

        helper.mapInsurancePolicy(req, policy);

        policy.setObject(object);

        policyRepository.save(policy);
    }

    public void saveMinimumBalance(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        ObjectOrderMinimumBalance balance = balanceRepository
                .findByObjectId(object.getId())
                .orElseGet(ObjectOrderMinimumBalance::new);

        helper.mapOrderMinimumBalance(req, balance);

        balance.setObject(object);

        balanceRepository.save(balance);
    }

    public void saveKchs(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        if (object.isEmergencyCommission()) {

            List<ObjectCompositionKchs> newList =
                    helper.mapKchsList(req);

            List<ObjectCompositionKchs> oldList =
                    kchsRepository.findAllByObjectId(objectId);

            SyncListUtils.syncList(
                    newList,
                    oldList,
                    ObjectCompositionKchs::getId,
                    kchsRepository::deleteById
            );

            for (ObjectCompositionKchs item : newList) {
                item.setObject(object);
                kchsRepository.save(item);
            }

        } else {

            List<ObjectCompositionKchs> oldList =
                    kchsRepository.findAllByObjectId(objectId);

            SyncListUtils.syncList(
                    List.of(),
                    oldList,
                    ObjectCompositionKchs::getId,
                    kchsRepository::deleteById
            );
        }
    }

    public void saveEquipment(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        List<ObjectTechnologicalEquipment> newList =
                helper.mapEquipmentList(req);

        List<ObjectTechnologicalEquipment> oldList =
                equipmentRepository.findAllByObjectId(objectId);

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectTechnologicalEquipment::getId,
                equipmentRepository::deleteById
        );

        for (ObjectTechnologicalEquipment item : newList) {
            item.setObject(object);
            equipmentRepository.save(item);
        }
    }

    public void saveTechnoBlocks(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        List<ObjectTechnologicalBlock> newList =
                helper.mapTechnoBlockList(req);

        List<ObjectTechnologicalBlock> oldList =
                technoBlockRepository.findAllByObjectId(objectId);

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectTechnologicalBlock::getId,
                technoBlockRepository::deleteById
        );

        for (ObjectTechnologicalBlock item : newList) {
            item.setObject(object);
            technoBlockRepository.save(item);
        }
    }

    public void saveStructures(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        List<ObjectStructure> newList =
                helper.mapStructureList(req);

        List<ObjectStructure> oldList =
                structureRepository.findAllByObjectId(objectId);

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectStructure::getId,
                structureRepository::deleteById
        );

        for (int i = 0; i < newList.size(); i++) {

            ObjectStructure structure = newList.get(i);

            structure.setObject(object);

            structureRepository.save(structure);

            List<ObjectScenario> newScenarios =
                    helper.mapScenarios(
                            req,
                            structure,
                            scenarioRepository,
                            i
                    );

            List<ObjectScenario> oldScenarios =
                    objectScenarioRepository.findAllByStructureId(
                            structure.getId()
                    );

            SyncListUtils.syncList(
                    newScenarios,
                    oldScenarios,
                    ObjectScenario::getId,
                    objectScenarioRepository::deleteById
            );

            objectScenarioRepository.saveAll(newScenarios);
        }
    }

    public void saveFireEquipment(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        List<ObjectFireEquipment> newList =
                helper.mapFireEquipmentList(req);

        List<ObjectFireEquipment> oldList =
                fireEquipmentRepository
                        .findAllByObjectIdOrderByNumber(objectId);

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectFireEquipment::getId,
                fireEquipmentRepository::deleteById
        );

        for (ObjectFireEquipment item : newList) {
            item.setObject(object);
            fireEquipmentRepository.save(item);
        }
    }

    public void savePersons(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        Integer objectId = object.getId();

        List<ObjectPersonsResponsible> newList =
                helper.mapPersonsResponseList(req);

        List<ObjectPersonsResponsible> oldList =
                personsRepository
                        .findAllByObjectIdOrderByNumber(objectId);

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectPersonsResponsible::getId,
                personsRepository::deleteById
        );

        for (ObjectPersonsResponsible item : newList) {
            item.setObject(object);
            personsRepository.save(item);
        }
    }

    public void saveImages(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        List<ObjectImage> images =
                imageRepository.findAllByObjectId(object.getId());

        helper.mapImages(req, images);

        imageRepository.saveAll(images);
    }

    public ObjectModel save(
            HttpServletRequest req,
            ObjectSaveHelper helper,
            ObjectModel object
    ) {

        object = saveObject(req, helper, object);
        saveAddress(req, helper, object);
        saveKchs(req, helper, object);
        saveEquipment(req, helper, object);
        saveStructures(req, helper, object);
        saveTechnoBlocks(req, helper, object);
        saveFireEquipment(req, helper, object);
        savePersons(req, helper, object);
        saveImages(req, helper, object);
        savePolicy(req, helper, object);
        saveMinimumBalance(req, helper, object);
        return object;
    }

    //REST
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