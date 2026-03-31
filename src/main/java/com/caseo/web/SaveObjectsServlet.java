package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.ObjectSaveHelper;
import com.caseo.web.util.SyncListUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static com.caseo.web.util.RequestUtils.*;

@WebServlet("/createObjects")
public class SaveObjectsServlet extends HttpServlet {

    private ObjectSaveHelper saveHelper;

    // Сервисы для объектов
    private ChildService<ObjectModel> objectService;

    // Родители
    private ParentService<Organization> orgService;
    private ParentService<ReferenceCity> cityService;
    private ParentService<Asf> asfService;
    private ParentService<ObjectHazardousSubstance> substanceService;
    private ParentService<ObjectModel> objectParentService;
    private ParentService<ObjectType> typeService;

    // Дети
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
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        saveHelper = new ObjectSaveHelper();

        // Инициализируем все сервисы
        orgService = services.getParentService(Organization.class);
        cityService = services.getParentService(ReferenceCity.class);
        asfService = services.getParentService(Asf.class);
        substanceService = services.getParentService(ObjectHazardousSubstance.class);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String orgIdParam = req.getParameter("orgId");
            int objectId = paramInt(req, "objectId");

            int orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : 0;

            if (orgId == 0) throw new ServletException("orgId is required");

            Organization organization = orgService.getOneById(orgId);

            // Получаем данные из формы
            String[] objectFullNames = req.getParameterValues("object_full_name");
            if (objectFullNames == null) return;

            // Получаем все объекты этой организации
            ObjectModel object = objectParentService.getOneById(objectId);

            if (object == null) {
                throw new ServletException("Объект с id: " + objectId + " не найден в базе!");
            }

            object.setOrganization(organization);

            saveHelper.mapObject(req, object);

            // справочники
            int cityId = paramInt(req, "object_city_id", 0);
            if (cityId > 0) object.setCity(cityService.getOneById(cityId));

            int asfId = paramInt(req,"object_asf_id", 0);
            if (asfId > 0) object.setAsf(asfService.getOneById(asfId));

            int substanceId = paramInt(req,"hazardous_substance_id", 0);
            if (substanceId > 0) object.setHazardousSubstance(substanceService.getOneById(substanceId));

            int typeId = paramInt(req,"object_type_id", 0);
            if (typeId > 0) object.setType(typeService.getOneById(typeId));

            // сохраняем
            objectService.save(object);

            // адрес
            ObjectAddress address = addressService.getOneByParentId(object.getId());
            if (address == null) address = new ObjectAddress();
            saveHelper.mapAddress(req, address);
            address.setObject(object);
            addressService.save(address);

            // КЧС
            if (object.isEmergencyCommission()) {
                List<ObjectCompositionKchs> kchsList = saveHelper.mapKchsList(req);

                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(object.getId());
                SyncListUtils.syncList(kchsList, oldDbList, ObjectCompositionKchs::getId, kchsService::deleteById);

                for (ObjectCompositionKchs kchs : kchsList) {
                    kchs.setObject(object);
                    kchsService.save(kchs);
                }
            } else {
                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(object.getId());
                if (oldDbList != null && !oldDbList.isEmpty()) {
                    for (ObjectCompositionKchs oldKchs : oldDbList) {
                        kchsService.deleteById(oldKchs.getId());
                    }
                }
            }

            // оборудование
            List<ObjectTechnologicalEquipment> equipmentList = saveHelper.mapEquipmentList(req);
            if (object.getId() != null) {
                List<ObjectTechnologicalEquipment> oldDbList = equipmentService.getManyByParentId(object.getId());
                SyncListUtils.syncList(equipmentList, oldDbList,
                        ObjectTechnologicalEquipment::getId,
                        equipmentService::deleteById
                );
            }
            for (ObjectTechnologicalEquipment equipment : equipmentList) {
                equipment.setObject(object);
                equipmentService.save(equipment);
            }

            // структура
            List<ObjectStructure> structureList = saveHelper.mapStructureList(req);
            if (object.getId() != null) {
                List<ObjectStructure> oldDbList = structureService.getManyByParentId(object.getId());
                SyncListUtils.syncList(structureList, oldDbList,
                        ObjectStructure::getId,
                        equipmentService::deleteById
                );
            }
            for (ObjectStructure structure : structureList) {
                structure.setObject(object);
                structureService.save(structure);
            }

            // технологические блоки
            List<ObjectTechnologicalBlock> technoBlockList = saveHelper.mapTechnoBlockList(req);
            if (object.getId() != null) {
                List<ObjectTechnologicalBlock> oldDbList = technoBlockService.getManyByParentId(object.getId());
                SyncListUtils.syncList(technoBlockList, oldDbList,
                        ObjectTechnologicalBlock::getId,
                        technoBlockService::deleteById
                );
            }
            for (ObjectTechnologicalBlock technoBlock : technoBlockList) {
                technoBlock.setObject(object);
                technoBlockService.save(technoBlock);
            }

            // ответственные за план
            List<ObjectPersonsResponsible> personsResponsiblesList = saveHelper.mapPersonsResponseList(req);
            if (object.getId() != null) {
                List<ObjectPersonsResponsible> oldDbList = personsResponsibleService.getManyByParentId(object.getId());
                SyncListUtils.syncList(personsResponsiblesList, oldDbList,
                        ObjectPersonsResponsible::getId,
                        equipmentService::deleteById
                );
            }
            for (ObjectPersonsResponsible personsResponsible : personsResponsiblesList) {
                personsResponsible.setObject(object);
                personsResponsibleService.save(personsResponsible);
            }

            List<ObjectImage> images = imageService.getManyByParentId(object.getId());

            saveHelper.mapImages(req, images);

            for (ObjectImage image : images) {
                imageService.save(image);
            }

            // страховка
            ObjectInsurancePolicy policy = policyService.getOneByParentId(object.getId());
            if (policy == null) policy = new ObjectInsurancePolicy();
            saveHelper.mapInsurancePolicy(req, policy);
            policy.setObject(object);
            policyService.save(policy);

            // приказ
            ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(object.getId());
            if (balance == null) balance = new ObjectOrderMinimumBalance();
            saveHelper.mapOrderMinimumBalance(req, balance);
            balance.setObject(object);
            balanceService.save(balance);

            resp.sendRedirect("objects?mode=view&orgId=" + orgId + "&id=" + object.getId());
        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении объектов", e);
            throw new ServletException("Ошибка при сохранении объектов", e);
        }
    }
}