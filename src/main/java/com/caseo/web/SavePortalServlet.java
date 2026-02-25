//package com.caseo.web;
//
//import com.caseo.app.ApplicationContext;
//import com.caseo.app.InternalServices;
//import com.caseo.domain.model.*;
//import com.caseo.domain.service.*;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.MultipartConfig;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.Part;
//import java.io.IOException;
//
//@WebServlet("/savePortal")
//@MultipartConfig(
//        maxFileSize = 1024 * 1024 * 8,
//        maxRequestSize = 1024 * 1024 * 20
//)
//public class SavePortalServlet extends HttpServlet {
//
//    private OrganizationService organizationService;
//    private OrganizationAddressService organizationAddressService;
//    private OrganizationSignerService organizationSignerService;
//    private OrganizationContactService organizationContactService;
//    private DocumentSetService documentSetService;
//    private ObjectService objectService;
//    private ObjectAddressService objectAddressService;
//    private ObjectCompositionKchsService objectCompositionKchsService;
//    private ObjectTechnologicalEquipmentService objectTechnologicalEquipmentService;
//    private ObjectStructureService objectStructureService;
//    private ObjectFireEquipmentService objectFireEquipmentService;
//    private ObjectRegionAuthoritiesService objectRegionAuthoritiesService;
//    private ObjectInsurancePolicyService objectInsurancePolicyService;
//    private ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService;
//    private ObjectTypeService objectTypeService;
//    private AsfService asfService;
//    private AsfCertificateService asfCertificateService;
//    private AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService;
//    private AsfDocumentImageService asfDocumentImageService;
//    private AsfPersonnelService asfPersonnelService;
//    private AsfSpecialistsService asfSpecialistsService;
//    private AsfSignerService asfSignerService;
//    private AsfWorkTypeService asfWorkTypeService;
//
//    @Override
//    public void init() {
//        ApplicationContext context = (ApplicationContext) getServletContext()
//                .getAttribute("appContext");
//        InternalServices services = context.internalServices();
//
//        organizationService = services.organizationService();
//        organizationAddressService = services.organizationAddressService();
//        organizationSignerService = services.organizationSignerService();
//        organizationContactService = services.organizationContactService();
//        documentSetService = services.documentSetService();
//        objectService = services.objectService();
//        objectAddressService = services.objectAddressService();
//        objectCompositionKchsService = services.objectCompositionKchsService();
//        objectTechnologicalEquipmentService = services.objectTechnologicalEquipmentService();
//        objectStructureService = services.objectStructureService();
//        objectFireEquipmentService = services.objectFireEquipmentService();
//        objectRegionAuthoritiesService = services.objectRegionAuthoritiesService();
//        objectInsurancePolicyService = services.objectInsurancePolicyService();
//        objectOrderMinimumBalanceService = services.objectOrderMinimumBalanceService();
//        objectTypeService = services.objectTypeService();
//        asfService = services.asfService();
//        asfCertificateService = services.asfCertificateService();
//        asfCompositionDeploymentFundsService = services.asfCompositionDeploymentFundsService();
//        asfDocumentImageService = services.asfDocumentImageService();
//        asfPersonnelService = services.asfPersonnelService();
//        asfSpecialistsService = services.asfSpecialistsService();
//        asfSignerService = services.asfSignerService();
//        asfWorkTypeService = services.asfWorkTypeService();
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        String mode = req.getParameter("mode");
//        String docId = req.getParameter("docId");
//        String orgId = req.getParameter("orgId");
//        String asfId = req.getParameter("asfId");
//
//        try {
//            // Сохраняем организацию
//            int savedOrgId = saveOrganization(req);
//
//            // Сохраняем документ и объекты
//            int savedDocId = saveDocument(req, savedOrgId);
//
//            // Сохраняем АСФ
//            int savedAsfId = saveAsf(req);
//
//            // Формируем URL для возврата
//            String redirectUrl = "portal?mode=" + mode;
//            if (savedDocId > 0) redirectUrl += "&docId=" + savedDocId;
//            if (savedOrgId > 0) redirectUrl += "&orgId=" + savedOrgId;
//            if (savedAsfId > 0) redirectUrl += "&asfId=" + savedAsfId;
//
//            resp.sendRedirect(redirectUrl);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            req.setAttribute("error", "Ошибка: " + e.getMessage());
//            req.getRequestDispatcher("/WEB-INF/pages/portal.jsp").forward(req, resp);
//        }
//    }
//
//    private int saveOrganization(HttpServletRequest req) throws Exception {
//        String orgIdParam = req.getParameter("orgId");
//        int orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : 0;
//
//        Organization org = new Organization(
//                orgId,
//                req.getParameter("organization_full_name"),
//                req.getParameter("organization_short_name"),
//                req.getParameter("organization_type_activity")
//        );
//
//        Organization savedOrg = organizationService.save(org);
//        int savedOrgId = savedOrg.id();
//
//        // Сохраняем адрес
//        OrganizationAddress addr = new OrganizationAddress(
//                req.getParameter("org_index"),
//                req.getParameter("org_constituent_entity"),
//                req.getParameter("org_city"),
//                req.getParameter("org_street"),
//                req.getParameter("org_house")
//        );
//        organizationAddressService.save(addr, savedOrgId);
//
//        // Сохраняем подписанта
//        OrganizationSigner signer = new OrganizationSigner(
//                req.getParameter("signer_position"),
//                req.getParameter("signer_name")
//        );
//        organizationSignerService.save(signer, savedOrgId);
//
//        // Сохраняем контакты
//        String[] contactNames = req.getParameterValues("org_contact_name[]");
//        String[] contactPositions = req.getParameterValues("org_contact_position[]");
//        String[] contactPhones = req.getParameterValues("org_contact_phone[]");
//        String[] contactAddresses = req.getParameterValues("org_contact_address[]");
//
//        if (contactNames != null) {
//            for (int i = 0; i < contactNames.length; i++) {
//                if (contactNames[i] != null && !contactNames[i].trim().isEmpty()) {
//                    OrganizationContact contact = new OrganizationContact(
//                            contactNames[i],
//                            contactPositions != null && contactPositions.length > i ? contactPositions[i] : null,
//                            contactPhones != null && contactPhones.length > i ? contactPhones[i] : null,
//                            contactAddresses != null && contactAddresses.length > i ? contactAddresses[i] : null
//                    );
//                    organizationContactService.save(contact, savedOrgId);
//                }
//            }
//        }
//
//        return savedOrgId;
//    }
//
//    private int saveDocument(HttpServletRequest req, int orgId) throws Exception {
//        String docIdParam = req.getParameter("docId");
//        int docId = docIdParam != null && !docIdParam.isEmpty() ? Integer.parseInt(docIdParam) : 0;
//
//        DocumentSet document = new DocumentSet(docId, orgId);
//        DocumentSet savedDoc = documentSetService.save(document);
//        int savedDocId = savedDoc.id();
//
//        // Сохраняем объекты
//        String[] objectFullNames = req.getParameterValues("object_full_name[]");
//        if (objectFullNames != null) {
//            for (int i = 0; i < objectFullNames.length; i++) {
//                if (objectFullNames[i] == null || objectFullNames[i].trim().isEmpty()) continue;
//
//                ObjectModel object = new ObjectModel(
//                        0,
//                        savedDocId,
//                        objectFullNames[i],
//                        req.getParameterValues("object_short_name[]")[i],
//                        Integer.parseInt(req.getParameterValues("object_city_id[]")[i]),
//                        Integer.parseInt(req.getParameterValues("hazard_class[]")[i]),
//                        Integer.parseInt(req.getParameterValues("object_asf_id[]")[i]),
//                        Integer.parseInt(req.getParameterValues("object_signer_id[]")[i]),
//                        req.getParameterValues("nearest_fire_station[]")[i],
//                        req.getParameterValues("department_gochs[]")[i],
//                        req.getParameterValues("emergency_commission[]")[i]
//                );
//
//                ObjectModel savedObject = objectService.save(object);
//
//                // Сохраняем адрес объекта
//                ObjectAddress addr = new ObjectAddress(
//                        savedObject.id(),
//                        req.getParameterValues("object_constituent_entity[]")[i],
//                        req.getParameterValues("object_area[]")[i],
//                        req.getParameterValues("object_coordinates[]")[i]
//                );
//                objectAddressService.save(addr);
//
//                // Сохраняем остальные связанные данные
//                saveObjectDetails(req, i, savedObject.id());
//            }
//        }
//
//        return savedDocId;
//    }
//
//    private void saveObjectDetails(HttpServletRequest req, int index, int objectId) throws Exception {
//        // КЧС
//        String[] kchsPositions = req.getParameterValues("kchs_position[]");
//        String[] kchsNames = req.getParameterValues("kchs_name[]");
//        String[] kchsPhones = req.getParameterValues("kchs_phone[]");
//        String[] kchsAddresses = req.getParameterValues("kchs_address[]");
//
//        if (kchsPositions != null) {
//            for (int i = 0; i < kchsPositions.length; i++) {
//                if (kchsPositions[i] != null && !kchsPositions[i].trim().isEmpty()) {
//                    ObjectCompositionKchs kchs = new ObjectCompositionKchs(
//                            objectId,
//                            kchsPositions[i],
//                            kchsNames != null && kchsNames.length > i ? kchsNames[i] : null,
//                            kchsPhones != null && kchsPhones.length > i ? kchsPhones[i] : null,
//                            kchsAddresses != null && kchsAddresses.length > i ? kchsAddresses[i] : null
//                    );
//                    objectCompositionKchsService.save(kchs);
//                }
//            }
//        }
//
//        // Оборудование
//        String[] technoNames = req.getParameterValues("techno_name[]");
//        String[] technoCharacteristics = req.getParameterValues("techno_characteristics[]");
//
//        if (technoNames != null) {
//            for (int i = 0; i < technoNames.length; i++) {
//                if (technoNames[i] != null && !technoNames[i].trim().isEmpty()) {
//                    ObjectTechnologicalEquipment eq = new ObjectTechnologicalEquipment(
//                            objectId,
//                            technoNames[i],
//                            technoCharacteristics != null && technoCharacteristics.length > i ? technoCharacteristics[i] : null
//                    );
//                    objectTechnologicalEquipmentService.save(eq);
//                }
//            }
//        }
//
//        // Тип объекта
//        String[] typeDefinitions = req.getParameterValues("object_type_definition[]");
//        if (typeDefinitions != null && typeDefinitions.length > index) {
//            ObjectType type = new ObjectType(objectId, typeDefinitions[index]);
//            objectTypeService.save(type);
//        }
//
//        // Страховка
//        String[] insuranceNumbers = req.getParameterValues("insurance_number[]");
//        String[] insuranceValidUntil = req.getParameterValues("insurance_valid_until[]");
//        if (insuranceNumbers != null && insuranceNumbers.length > index) {
//            ObjectInsurancePolicy policy = new ObjectInsurancePolicy(
//                    objectId,
//                    insuranceNumbers[index],
//                    insuranceValidUntil != null && insuranceValidUntil.length > index ? insuranceValidUntil[index] : null
//            );
//            objectInsurancePolicyService.save(policy);
//        }
//
//        // Остаток
//        String[] balanceNumbers = req.getParameterValues("balance_number[]");
//        String[] balanceDates = req.getParameterValues("balance_date[]");
//        if (balanceNumbers != null && balanceNumbers.length > index) {
//            ObjectOrderMinimumBalance balance = new ObjectOrderMinimumBalance(
//                    objectId,
//                    Integer.parseInt(balanceNumbers[index]),
//                    balanceDates != null && balanceDates.length > index ? balanceDates[index] : null
//            );
//            objectOrderMinimumBalanceService.save(balance);
//        }
//    }
//
//    private int saveAsf(HttpServletRequest req) throws Exception {
//        String asfIdParam = req.getParameter("asfId");
//        int asfId = asfIdParam != null && !asfIdParam.isEmpty() ? Integer.parseInt(asfIdParam) : 0;
//
//        String hours = req.getParameter("arrival_hours");
//        String minutes = req.getParameter("arrival_minutes");
//        String arrivalTime = null;
//        if (hours != null && !hours.isEmpty() && minutes != null && !minutes.isEmpty()) {
//            arrivalTime = String.format("%s:%s:00", hours, minutes);
//        } else if (hours != null && !hours.isEmpty()) {
//            arrivalTime = String.format("%s:00:00", hours);
//        }
//
//        Asf asf = new Asf(
//                asfId,
//                req.getParameter("full_name"),
//                req.getParameter("full_name_gen"),
//                req.getParameter("short_name"),
//                req.getParameter("email"),
//                req.getParameter("status_short"),
//                arrivalTime
//        );
//
//        Asf savedAsf = asfService.save(asf);
//        int savedAsfId = savedAsf.id();
//
//        // Сохраняем свидетельство
//        String certNumber = req.getParameter("cert_number");
//        if (certNumber != null && !certNumber.trim().isEmpty()) {
//            AsfCertificate certificate = new AsfCertificate(
//                    certNumber,
//                    req.getParameter("cert_series"),
//                    req.getParameter("issued_by"),
//                    req.getParameter("issue_basis"),
//                    req.getParameter("issue_date"),
//                    req.getParameter("valid_until")
//            );
//            asfCertificateService.save(certificate, savedAsfId);
//        }
//
//        // Сохраняем состав и размещение
//        String[] responsibilityAreas = req.getParameterValues("responsibility_area[]");
//        if (responsibilityAreas != null && responsibilityAreas.length > 0 && responsibilityAreas[0] != null) {
//            AsfCompositionDeploymentFunds deployment = new AsfCompositionDeploymentFunds(
//                    responsibilityAreas[0],
//                    getParamValue(req, "deployment_place[]", 0),
//                    getParamValue(req, "duty_officer_phone[]", 0),
//                    getParamValue(req, "contact_phone[]", 0),
//                    getParamValue(req, "deployment_email[]", 0),
//                    getParamValue(req, "number_buildings[]", 0),
//                    getParamValue(req, "total_area[]", 0)
//            );
//            asfCompositionDeploymentFundsService.save(deployment, savedAsfId);
//        }
//
//        // Сохраняем кадровый состав
//        AsfPersonnel personnel = new AsfPersonnel(
//                Integer.parseInt(req.getParameter("staff_by_staffing")),
//                Integer.parseInt(req.getParameter("staff_by_list")),
//                Integer.parseInt(req.getParameter("certified_total")),
//                Integer.parseInt(req.getParameter("qualified_total")),
//                Integer.parseInt(req.getParameter("third_class")),
//                Integer.parseInt(req.getParameter("second_class")),
//                Integer.parseInt(req.getParameter("first_class")),
//                Integer.parseInt(req.getParameter("international_class"))
//        );
//        asfPersonnelService.save(personnel, savedAsfId);
//
//        // Сохраняем специалистов
//        AsfSpecialists specialists = new AsfSpecialists(
//                Integer.parseInt(req.getParameter("specialists_total")),
//                Integer.parseInt(req.getParameter("asr_tp")),
//                Integer.parseInt(req.getParameter("asr_lrn_ter")),
//                Integer.parseInt(req.getParameter("gzsr")),
//                Integer.parseInt(req.getParameter("psr")),
//                Integer.parseInt(req.getParameter("driver")),
//                Integer.parseInt(req.getParameter("asr_lrn_sea"))
//            );
//        asfSpecialistsService.save(specialists, savedAsfId);
//
//        // Сохраняем подписантов
//        String[] signerNames = req.getParameterValues("signer_name[]");
//        String[] signerPositions = req.getParameterValues("signer_position[]");
//
//        if (signerNames != null) {
//            for (int i = 0; i < signerNames.length; i++) {
//                if (signerNames[i] != null && !signerNames[i].trim().isEmpty()) {
//                    AsfSigner signer = new AsfSigner(
//                            0,
//                            savedAsfId,
//                            signerNames[i],
//                            signerPositions != null && signerPositions.length > i ? signerPositions[i] : null
//                    );
//                    asfSignerService.save(signer, savedAsfId);
//                }
//            }
//        }
//
//        // Сохраняем типы работ
//        String[] workTypeNames = req.getParameterValues("work_type_name[]");
//        if (workTypeNames != null) {
//            for (String workTypeName : workTypeNames) {
//                if (workTypeName != null && !workTypeName.trim().isEmpty()) {
//                    AsfWorkType workType = new AsfWorkType(0, workTypeName.trim());
//                    asfWorkTypeService.save(workType, savedAsfId);
//                }
//            }
//        }
//
//        // Сохраняем изображения
//        saveAsfImages(req, savedAsfId);
//
//        return savedAsfId;
//    }
//
//    private void saveAsfImages(HttpServletRequest req, int asfId) throws Exception {
//        // Приложение 1
//        String[] imageNames1 = req.getParameterValues("image_name_1[]");
//        if (imageNames1 != null) {
//            for (int i = 0; i < imageNames1.length; i++) {
//                Part filePart = req.getPart("image_file_1_" + i);
//                if (filePart != null && filePart.getSize() > 0) {
//                    AsfDocumentImage image = new AsfDocumentImage(
//                            "1",
//                            filePart.getInputStream().readAllBytes(),
//                            imageNames1[i]
//                    );
//                    asfDocumentImageService.save(image, asfId);
//                }
//            }
//        }
//
//        // Приложение 2
//        String[] imageNames2 = req.getParameterValues("image_name_2[]");
//        if (imageNames2 != null) {
//            for (int i = 0; i < imageNames2.length; i++) {
//                Part filePart = req.getPart("image_file_2_" + i);
//                if (filePart != null && filePart.getSize() > 0) {
//                    AsfDocumentImage image = new AsfDocumentImage(
//                            "2",
//                            filePart.getInputStream().readAllBytes(),
//                            imageNames2[i]
//                    );
//                    asfDocumentImageService.save(image, asfId);
//                }
//            }
//        }
//    }
//
//    private String getParamValue(HttpServletRequest req, String paramName, int index) {
//        String[] values = req.getParameterValues(paramName);
//        return values != null && values.length > index ? values[index] : null;
//    }
//}