package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;
import ru.ecospas.web.helper.ObjectSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectService {

    private final ObjectModelRepository objectRepository;

    private final ReferenceCityRepository cityRepository;
    private final AsfRepository asfRepository;
    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final ObjectTypeRepository typeRepository;

    private final ObjectAddressRepository addressRepository;
    private final ObjectInsurancePolicyRepository policyRepository;
    private final ObjectOrderMinimumBalanceRepository balanceRepository;

    private final ObjectCompositionKchsRepository kchsRepository;
    private final ObjectTechnologicalEquipmentRepository equipmentRepository;
    private final ObjectStructureRepository structureRepository;
    private final ObjectTechnologicalBlockRepository technoBlockRepository;
    private final ObjectFireEquipmentRepository fireEquipmentRepository;
    private final ObjectPersonsResponsibleRepository personsRepository;
    private final ObjectImageRepository imageRepository;

    private final ScenarioRepository scenarioRepository;
    private final ObjectScenarioRepository objectScenarioRepository;

    public ObjectModel load(Integer id) {
        return objectRepository.findById(id).orElse(null);
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
}