package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.ObjectSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class SaveObjectServlet extends BaseServlet {

    private ObjectSaveHelper saveHelper;

    private ChildService<ObjectModel> objectService;

    private ParentService<ReferenceCity> cityService;
    private ParentService<Asf> asfService;
    private ParentService<ReferenceHazardousSubstance> substanceService;
    private ParentService<ObjectModel> objectParentService;
    private ParentService<ObjectType> typeService;

    private ChildService<ObjectAddress> addressService;
    private ChildService<ObjectInsurancePolicy> policyService;
    private ChildService<ObjectOrderMinimumBalance> balanceService;
    private ChildService<ObjectCompositionKchs> kchsService;
    private ChildService<ObjectTechnologicalEquipment> equipmentService;
    private ChildService<ObjectStructure> structureService;
    private ChildService<ObjectTechnologicalBlock> technoBlockService;
    private ChildService<ObjectFireEquipment> fireEquipmentService;
    private ChildService<ObjectPersonsResponsible> personsResponsibleService;
    private ChildService<ObjectImage> imageService;
    private ChildService<ObjectScenario> scenarioServiceChild;
    private ParentService<Scenario> scenarioService;

    @Override
    public void init() {
        super.init();
        saveHelper = new ObjectSaveHelper();

        cityService = services.getParentService(ReferenceCity.class);
        asfService = services.getParentService(Asf.class);
        substanceService = services.getParentService(ReferenceHazardousSubstance.class);
        typeService = services.getParentService(ObjectType.class);

        objectService = services.getChildService(ObjectModel.class);
        objectParentService = services.getParentService(ObjectModel.class);

        addressService = services.getChildService(ObjectAddress.class);
        policyService = services.getChildService(ObjectInsurancePolicy.class);
        balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        kchsService = services.getChildService(ObjectCompositionKchs.class);
        equipmentService = services.getChildService(ObjectTechnologicalEquipment.class);
        structureService = services.getChildService(ObjectStructure.class);
        technoBlockService = services.getChildService(ObjectTechnologicalBlock.class);
        fireEquipmentService = services.getChildService(ObjectFireEquipment.class);
        personsResponsibleService = services.getChildService(ObjectPersonsResponsible.class);
        imageService = services.getChildService(ObjectImage.class);
        scenarioServiceChild = services.getChildService(ObjectScenario.class);
        scenarioService = services.getParentService(Scenario.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int objectId = paramInt(req, "objectId");

            ObjectModel object;
            Organization organization;
            int orgId;

            if (objectId > 0) {

                object = objectParentService.getOneById(objectId);

                if (object == null) {
                    resp.sendError(404);
                    return;
                }

                if (requireAccess(req, resp, object.getOrganization().getId()) == null) return;

                organization = object.getOrganization();
                orgId = organization.getId();

            } else {

                orgId = paramInt(req, "orgId");

                if (orgId == 0) throw new ServletException("orgId is required");

                organization = requireAccess(req, resp, orgId);
                if (organization == null) return;

                object = new ObjectModel();
                object.setOrganization(organization);
            }

            Integer objectIdValue = object.getId();
            if (objectIdValue == null) {
                throw new ServletException("ID объекта не может быть null!");
            }

            object.setOrganization(organization);

            saveHelper.mapObject(req, object);

            int cityId = paramInt(req, "object_city_id", 0);
            if (cityId > 0) object.setCity(cityService.getOneById(cityId));

            int asfId = paramInt(req, "object_asf_id", 0);
            if (asfId > 0) object.setAsf(asfService.getOneById(asfId));

            int substanceId = paramInt(req, "hazardous_substance_id", 0);
            if (substanceId > 0) object.setHazardousSubstance(substanceService.getOneById(substanceId));

            int typeId = paramInt(req, "object_type_id", 0);
            if (typeId > 0) object.setType(typeService.getOneById(typeId));

            objectService.save(object);

            ObjectAddress address = addressService.getOneByParentId(objectIdValue);
            if (address == null) address = new ObjectAddress();
            saveHelper.mapAddress(req, address);
            address.setObject(object);
            addressService.save(address);

            if (object.isEmergencyCommission()) {
                List<ObjectCompositionKchs> kchsList = saveHelper.mapKchsList(req);

                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(objectIdValue);
                SyncListUtils.syncList(kchsList, oldDbList,
                        ObjectCompositionKchs::getId,
                        kchsService::deleteById);

                for (ObjectCompositionKchs kchs : kchsList) {
                    kchs.setObject(object);
                    kchsService.save(kchs);
                }
            } else {
                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(objectIdValue);
                if (oldDbList != null && !oldDbList.isEmpty()) {
                    SyncListUtils.syncList(List.of(), oldDbList,
                            ObjectCompositionKchs::getId,
                            kchsService::deleteById
                    );
                }
            }

            List<ObjectTechnologicalEquipment> equipmentList = saveHelper.mapEquipmentList(req);
            List<ObjectTechnologicalEquipment> oldTechnoList = equipmentService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(equipmentList, oldTechnoList,
                    ObjectTechnologicalEquipment::getId,
                    equipmentService::deleteById
            );
            for (ObjectTechnologicalEquipment equipment : equipmentList) {
                equipment.setObject(object);
                equipmentService.save(equipment);
            }

            List<ObjectStructure> structureList = saveHelper.mapStructureList(req);
            List<ObjectStructure> oldStructureList = structureService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(structureList, oldStructureList,
                    ObjectStructure::getId,
                    structureService::deleteById
            );
            for (int i = 0; i < structureList.size(); i++) {
                ObjectStructure structure = structureList.get(i);

                structure.setObject(object);
                structureService.save(structure);
                // === SCENARIOS ===
                List<ObjectScenario> newScenarios =
                        saveHelper.mapScenarios(req, structure, scenarioService, i);

                List<ObjectScenario> oldScenarios =
                        scenarioServiceChild.getManyByParentId(structure.getId());

                SyncListUtils.syncList(
                        newScenarios,
                        oldScenarios,
                        ObjectScenario::getId,
                        scenarioServiceChild::deleteById
                );

                for (ObjectScenario sc : newScenarios) {
                    scenarioServiceChild.save(sc);
                }
            }

            List<ObjectTechnologicalBlock> technoBlockList = saveHelper.mapTechnoBlockList(req);
            List<ObjectTechnologicalBlock> oldBlockList = technoBlockService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(technoBlockList, oldBlockList,
                    ObjectTechnologicalBlock::getId,
                    technoBlockService::deleteById
            );
            for (ObjectTechnologicalBlock block : technoBlockList) {
                block.setObject(object);
                technoBlockService.save(block);
            }

            List<ObjectFireEquipment> fireEquipmentList = saveHelper.mapFireEquipmentList(req);
            List<ObjectFireEquipment> oldFireEquipmentList = fireEquipmentService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(fireEquipmentList, oldFireEquipmentList,
                    ObjectFireEquipment::getId,
                    fireEquipmentService::deleteById
            );
            for (ObjectFireEquipment fireEquipment : fireEquipmentList) {
                fireEquipment.setObject(object);
                fireEquipmentService.save(fireEquipment);
            }

            List<ObjectPersonsResponsible> personsResponsiblesList = saveHelper.mapPersonsResponseList(req);
            List<ObjectPersonsResponsible> oldPersonsList = personsResponsibleService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(personsResponsiblesList, oldPersonsList,
                    ObjectPersonsResponsible::getId,
                    personsResponsibleService::deleteById
            );
            for (ObjectPersonsResponsible personsResponsible : personsResponsiblesList) {
                personsResponsible.setObject(object);
                personsResponsibleService.save(personsResponsible);
            }

            List<ObjectImage> images = imageService.getManyByParentId(objectIdValue);

            saveHelper.mapImages(req, images);

            for (ObjectImage image : images) imageService.save(image);

            ObjectInsurancePolicy policy = policyService.getOneByParentId(objectIdValue);
            if (policy == null) policy = new ObjectInsurancePolicy();
            saveHelper.mapInsurancePolicy(req, policy);
            policy.setObject(object);
            policyService.save(policy);

            ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(objectIdValue);
            if (balance == null) balance = new ObjectOrderMinimumBalance();
            saveHelper.mapOrderMinimumBalance(req, balance);
            balance.setObject(object);
            balanceService.save(balance);

            resp.sendRedirect("object?mode=view&orgId=" + orgId + "&id=" + objectIdValue);
        } catch (Exception e) {
            getServletContext().log("Error saving Object", e);
            throw new ServletException("Error saving Object", e);
        }
    }
}