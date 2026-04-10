package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet("/create-objects")
public class SaveObjectsServlet extends BaseServlet {

    private ObjectSaveHelper saveHelper;

    private ChildService<ObjectModel> objectService;

    private ParentService<Organization> orgService;
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
    private ChildService<ObjectPersonsResponsible> personsResponsibleService;
    private ChildService<ObjectImage> imageService;

    @Override
    public void init() {
        super.init();
        saveHelper = new ObjectSaveHelper();

        orgService = services.getParentService(Organization.class);
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
        personsResponsibleService = services.getChildService(ObjectPersonsResponsible.class);
        imageService = services.getChildService(ObjectImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            String orgIdParam = req.getParameter("orgId");
            int objectId = paramInt(req, "objectId");

            int orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : 0;

            if (orgId == 0) throw new ServletException("orgId is required");

            Organization organization = orgService.getOneById(orgId);
            if (organization == null) {
                throw new ServletException("Организация с id: " + orgId + " не найдена!");
            }

            ObjectModel object = objectParentService.getOneById(objectId);

            if (object == null) {
                throw new ServletException("Объект с id: " + objectId + " не найден в базе!");
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
                SyncListUtils.syncList(kchsList, oldDbList, ObjectCompositionKchs::getId, kchsService::deleteById);

                for (ObjectCompositionKchs kchs : kchsList) {
                    kchs.setObject(object);
                    kchsService.save(kchs);
                }
            } else {
                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(objectIdValue);
                if (oldDbList != null && !oldDbList.isEmpty()) {
                    SyncListUtils.syncList(
                            List.of(),
                            oldDbList,
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
            for (ObjectStructure structure : structureList) {
                structure.setObject(object);
                structureService.save(structure);
            }

            List<ObjectTechnologicalBlock> technoBlockList = saveHelper.mapTechnoBlockList(req);
            List<ObjectTechnologicalBlock> oldBlockList = technoBlockService.getManyByParentId(objectIdValue);
            SyncListUtils.syncList(technoBlockList, oldBlockList,
                    ObjectTechnologicalBlock::getId,
                    technoBlockService::deleteById
            );
            for (ObjectTechnologicalBlock technoBlock : technoBlockList) {
                technoBlock.setObject(object);
                technoBlockService.save(technoBlock);
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

            resp.sendRedirect("objects?mode=view&orgId=" + orgId + "&id=" + objectIdValue);
        } catch (Exception e) {
            getServletContext().log("Error saving Object", e);
            throw new ServletException("Error saving Object", e);
        }
    }
}