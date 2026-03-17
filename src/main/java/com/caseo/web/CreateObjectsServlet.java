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
public class CreateObjectsServlet extends HttpServlet {

    private ObjectSaveHelper saveHelper;

    // Сервисы для объектов
    private ChildService<ObjectModel> objectService;

    // Родители
    private ParentService<Organization> orgService;
    private ParentService<ReferenceCity> cityService;
    private ParentService<Asf> asfService;
    private ParentService<ObjectHazardousSubstance> substanceService;

    // Дети
    private ChildService<ObjectAddress> addressService;
    private ChildService<ObjectType> typeService;
    private ChildService<ObjectInsurancePolicy> policyService;
    private ChildService<ObjectOrderMinimumBalance> balanceService;
    private ChildService<ObjectCompositionKchs> kchsService;
    private ChildService<ObjectTechnologicalEquipment> equipmentService;

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

        objectService = services.getChildService(ObjectModel.class);

        addressService = services.getChildService(ObjectAddress.class);
        typeService = services.getChildService(ObjectType.class);
        policyService = services.getChildService(ObjectInsurancePolicy.class);
        balanceService = services.getChildService(ObjectOrderMinimumBalance.class);
        kchsService = services.getChildService(ObjectCompositionKchs.class);
        equipmentService = services.getChildService(ObjectTechnologicalEquipment.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String orgIdParam = req.getParameter("orgId");
            Integer objectId = paramInt(req, "objectId");
            System.out.println(objectId);

            Integer orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : null;

            if (orgId == null) throw new ServletException("orgId is required");

            Organization organization = orgService.getOneById(orgId);

            // Получаем данные из формы
            String[] objectFullNames = req.getParameterValues("object_full_name");
            if (objectFullNames == null) return;

            // Обрабатываем каждый объект из формы
            List<ObjectModel> existingObjects = objectService.getManyByParentId(orgId);

            ObjectModel object = null;

            for (ObjectModel obj : existingObjects) {
                if (obj.getId().equals(objectId)) {
                    object = obj;
                    break;
                }
            }

            if (object == null) {
                throw new ServletException("Object not found with id: " + objectId);
            }

            object.setOrganization(organization);

            saveHelper.mapObject(req, 0, object);

            // справочники
            Integer cityId = paramInt(req, "presence_area_id", 0);
            if (cityId != null) object.setCity(cityService.getOneById(cityId));

            Integer asfId = paramInt(req,"object_asf_id", 0);
            if (asfId != null) object.setAsf(asfService.getOneById(asfId));

            Integer substanceId = paramInt(req,"hazardous_substance_id", 0);
            if (substanceId != null) object.setHazardousSubstance(substanceService.getOneById(substanceId));

            // сохраняем
            objectService.save(object);

            // адрес
            ObjectAddress address = addressService.getOneByParentId(object.getId());
            if (address == null) address = new ObjectAddress();
            saveHelper.mapAddress(req, 0, address);
            address.setObject(object);
            addressService.save(address);

            // КЧС
            List<ObjectCompositionKchs> kchsList = saveHelper.mapKchsList(req, 0);
            if (object.getId() != null) {
                List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(object.getId());
                SyncListUtils.syncList(kchsList, oldDbList,
                        ObjectCompositionKchs::getId,
                        kchsService::deleteById
                );
            }
            for (ObjectCompositionKchs kchs : kchsList) {
                kchs.setObject(object);
                kchsService.save(kchs);
            }

            // оборудование
            List<ObjectTechnologicalEquipment> equipmentList = saveHelper.mapEquipmentList(req, 0);
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

            // тип
            ObjectType objectType = typeService.getOneByParentId(object.getId());
            if (objectType == null) objectType = new ObjectType();
            saveHelper.mapObjectType(req, 0, objectType);
            objectType.setObject(object);
            typeService.save(objectType);

            // страховка
            ObjectInsurancePolicy policy = policyService.getOneByParentId(object.getId());
            if (policy == null) policy = new ObjectInsurancePolicy();
            saveHelper.mapInsurancePolicy(req, 0, policy);
            policy.setObject(object);
            policyService.save(policy);

            // приказ
            ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(object.getId());
            if (balance == null) balance = new ObjectOrderMinimumBalance();
            saveHelper.mapOrderMinimumBalance(req, 0, balance);
            balance.setObject(object);
            balanceService.save(balance);
            resp.sendRedirect("objects?mode=view&orgId=" + orgId + "&id=" + object.getId());
        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении объектов", e);
            throw new ServletException("Ошибка при сохранении объектов", e);
        }
    }
}