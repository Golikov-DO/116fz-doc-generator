package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.ObjectSaveHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
            req.getParameterMap().forEach((k,v) ->
                    System.out.println(k + " = " + java.util.Arrays.toString(v))
            );
            String orgIdParam = req.getParameter("orgId");
            String mode = req.getParameter("mode");

            Integer orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : null;

            if (orgId == null) throw new ServletException("orgId is required");

            Organization organization = orgService.getOneById(orgId);
            List<ObjectModel> objects;
            List<ObjectModel> existingObjects = objectService.getManyByParentId(orgId);

            objects = existingObjects != null
                    ? new ArrayList<>(existingObjects)
                    : new ArrayList<>();

            // Получаем данные из формы
            String[] objectFullNames = req.getParameterValues("object_full_name[]");
            if (objectFullNames == null) return;

            // Обрабатываем каждый объект из формы
            for (int i = 0; i < objectFullNames.length; i++) {
                if (objectFullNames[i] == null || objectFullNames[i].trim().isEmpty()) continue;

                ObjectModel object;

                // Если объект уже существует - используем его
                if (i < objects.size()) object = objects.get(i);
                else object = new ObjectModel();

                // Заполняем основную информацию
                object.setOrganization(organization);
                saveHelper.mapObject(req, i, object);

                // Справочники
                Integer cityId = paramInt(req, "presence_area_id[]", i);
                if (cityId != null) object.setCity(cityService.getOneById(cityId));

                Integer asfId = paramInt(req,"object_asf_id[]", i);
                if (asfId != null) object.setAsf(asfService.getOneById(asfId));

                Integer substanceId = paramInt(req,"hazardous_substance_id[]", i);
                if (substanceId != null) object.setHazardousSubstance(substanceService.getOneById(substanceId));

                // Сохраняем объект (получает ID)
                objectService.save(object);

                // Адрес
                ObjectAddress address = addressService.getOneByParentId(object.getId());
                if (address == null) {
                    address = new ObjectAddress();
                }
                saveHelper.mapAddress(req, i, address);
                address.setObject(object);
                addressService.save(address);

                // КЧС
                List<ObjectCompositionKchs> kchsList = saveHelper.mapKchsList(req, i);

                if (object.getId() != null) {
                    List<ObjectCompositionKchs> oldDbList = kchsService.getManyByParentId(object.getId());
                    for (ObjectCompositionKchs oldItem : oldDbList) {
                        boolean stillExists = kchsList.stream()
                                .anyMatch(n -> n.getId() != null && n.getId().equals(oldItem.getId()));

                        if (!stillExists) {
                            kchsService.deleteById(oldItem.getId());
                        }
                    }
                }

                for (ObjectCompositionKchs kchs : kchsList) {
                    kchs.setObject(object);
                    kchsService.save(kchs);
                }

                // Оборудование
                List<ObjectTechnologicalEquipment> equipmentList = saveHelper.mapEquipmentList(req, i);

                if (object.getId() != null) {
                    List<ObjectTechnologicalEquipment> oldDbList = equipmentService.getManyByParentId(object.getId());
                    for (ObjectTechnologicalEquipment oldItem : oldDbList) {
                        boolean stillExists = equipmentList.stream()
                                .anyMatch(n -> n.getId() != null && n.getId().equals(oldItem.getId()));

                        if (!stillExists) {
                            equipmentService.deleteById(oldItem.getId());
                        }
                    }
                }

                for (ObjectTechnologicalEquipment equipment : equipmentList) {
                    equipment.setObject(object);
                    equipmentService.save(equipment);
                }

                // Тип объекта
                ObjectType objectType = typeService.getOneByParentId(object.getId());
                if (objectType == null) {
                    objectType = new ObjectType();
                }
                saveHelper.mapObjectType(req, i, objectType);
                objectType.setObject(object);
                typeService.save(objectType);

                // Страховка
                ObjectInsurancePolicy policy = policyService.getOneByParentId(object.getId());
                if (policy == null) {
                    policy = new ObjectInsurancePolicy();
                }
                saveHelper.mapInsurancePolicy(req, i, policy);
                policy.setObject(object);
                policyService.save(policy);

                // Приказ
                ObjectOrderMinimumBalance balance = balanceService.getOneByParentId(object.getId());
                if (balance == null) {
                    balance = new ObjectOrderMinimumBalance();
                }
                saveHelper.mapOrderMinimumBalance(req, i, balance);
                balance.setObject(object);
                balanceService.save(balance);

            }

            resp.sendRedirect("portal?mode=" + mode + "&orgId=" + orgId + "&tab=objects");

        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении объектов", e);
            throw new ServletException("Ошибка при сохранении объектов", e);
        }
    }
}