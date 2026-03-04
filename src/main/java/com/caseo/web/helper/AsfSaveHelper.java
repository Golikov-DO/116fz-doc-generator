package com.caseo.web.helper;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class AsfSaveHelper {

    private final AsfCertificateService asfCertificateService;
    private final AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService;
    private final AsfPersonnelService asfPersonnelService;
    private final AsfSpecialistsService asfSpecialistsService;
    private final AsfSignerService asfSignerService;
    private final AsfWorkTypeService asfWorkTypeService;
    private final AsfDocumentImageService asfDocumentImageService;

    public AsfSaveHelper(
            AsfCertificateService asfCertificateService,
            AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService,
            AsfPersonnelService asfPersonnelService,
            AsfSpecialistsService asfSpecialistsService,
            AsfSignerService asfSignerService,
            AsfWorkTypeService asfWorkTypeService,
            AsfDocumentImageService asfDocumentImageService) {
        this.asfCertificateService = asfCertificateService;
        this.asfCompositionDeploymentFundsService = asfCompositionDeploymentFundsService;
        this.asfPersonnelService = asfPersonnelService;
        this.asfSpecialistsService = asfSpecialistsService;
        this.asfSignerService = asfSignerService;
        this.asfWorkTypeService = asfWorkTypeService;
        this.asfDocumentImageService = asfDocumentImageService;
    }

    public void saveRelatedEntities(HttpServletRequest req, int asfId) throws Exception {
        saveCertificate(req, asfId);
        saveDeployment(req, asfId);
        savePersonnel(req, asfId);
        saveSpecialists(req, asfId);
        saveSigners(req, asfId);
        saveWorkTypes(req, asfId);
        saveImages(req, asfId);
    }

    private void saveCertificate(HttpServletRequest req, int asfId) throws Exception {
        String certNumber = req.getParameter("cert_number");
        if (certNumber != null && !certNumber.trim().isEmpty()) {
            AsfCertificate certificate = new AsfCertificate(
                    certNumber,
                    req.getParameter("cert_series"),
                    req.getParameter("issued_by"),
                    req.getParameter("issue_basis"),
                    req.getParameter("issue_date"),
                    req.getParameter("valid_until")
            );
            asfCertificateService.save(certificate, asfId);
        }
    }

    private void saveDeployment(HttpServletRequest req, int asfId) throws Exception {
        String[] responsibilityAreas = req.getParameterValues("responsibility_area[]");
        if (responsibilityAreas != null && responsibilityAreas.length > 0 && responsibilityAreas[0] != null) {
            AsfCompositionDeploymentFunds deployment = new AsfCompositionDeploymentFunds(
                    responsibilityAreas[0],
                    getParamValue(req, "deployment_place[]", 0),
                    getParamValue(req, "duty_officer_phone[]", 0),
                    getParamValue(req, "contact_phone[]", 0),
                    getParamValue(req, "deployment_email[]", 0),
                    getParamValue(req, "number_buildings[]", 0),
                    getParamValue(req, "total_area[]", 0)
            );
            asfCompositionDeploymentFundsService.save(deployment, asfId);
        }
    }

    private void savePersonnel(HttpServletRequest req, int asfId) throws Exception {
        AsfPersonnel personnel = new AsfPersonnel(
                parseIntOrDefault(req.getParameter("staff_by_staffing"), 0),
                parseIntOrDefault(req.getParameter("staff_by_list"), 0),
                parseIntOrDefault(req.getParameter("certified_total"), 0),
                parseIntOrDefault(req.getParameter("qualified_total"), 0),
                parseIntOrDefault(req.getParameter("third_class"), 0),
                parseIntOrDefault(req.getParameter("second_class"), 0),
                parseIntOrDefault(req.getParameter("first_class"), 0),
                parseIntOrDefault(req.getParameter("international_class"), 0)
        );
        asfPersonnelService.save(personnel, asfId);
    }

    private void saveSpecialists(HttpServletRequest req, int asfId) throws Exception {
        AsfSpecialists specialists = new AsfSpecialists(
                parseIntOrDefault(req.getParameter("specialists_total"), 0),
                parseIntOrDefault(req.getParameter("asr_tp"), 0),
                parseIntOrDefault(req.getParameter("asr_lrn_ter"), 0),
                parseIntOrDefault(req.getParameter("gzsr"), 0),
                parseIntOrDefault(req.getParameter("psr"), 0),
                parseIntOrDefault(req.getParameter("driver"), 0),
                parseIntOrDefault(req.getParameter("asr_lrn_sea"), 0)
        );
        asfSpecialistsService.save(specialists, asfId);
    }

    private void saveSigners(HttpServletRequest req, int asfId) throws Exception {
        String[] signerNames = req.getParameterValues("signer_name[]");
        String[] signerPositions = req.getParameterValues("signer_position[]");

        if (signerNames != null) {
            for (int i = 0; i < signerNames.length; i++) {
                if (signerNames[i] != null && !signerNames[i].trim().isEmpty()) {
                    AsfSigner signer = new AsfSigner(
                            0,
                            asfId,
                            signerNames[i],
                            signerPositions != null && signerPositions.length > i ? signerPositions[i] : null
                    );
                    asfSignerService.save(signer, asfId);
                }
            }
        }
    }

    private void saveWorkTypes(HttpServletRequest req, int asfId) throws Exception {
        String[] workTypeNames = req.getParameterValues("work_type_name[]");
        if (workTypeNames != null) {
            for (String workTypeName : workTypeNames) {
                if (workTypeName != null && !workTypeName.trim().isEmpty()) {
                    AsfWorkType workType = new AsfWorkType(asfId, workTypeName.trim());
                    asfWorkTypeService.save(workType, asfId);
                }
            }
        }
    }

    private void saveImages(HttpServletRequest req, int asfId) throws Exception {
        // Удаляем старые изображения
        asfDocumentImageService.deleteByAsfId(asfId);

        // Приложение 1
        String[] imageNames1 = req.getParameterValues("image_name_1[]");
        if (imageNames1 != null) {
            for (int i = 0; i < imageNames1.length; i++) {
                Part filePart = req.getPart("image_file_1_" + i);
                if (filePart != null && filePart.getSize() > 0) {
                    AsfDocumentImage image = new AsfDocumentImage(
                            "1",
                            filePart.getInputStream().readAllBytes(),
                            imageNames1[i]
                    );
                    asfDocumentImageService.save(image, asfId);
                }
            }
        }

        // Приложение 2
        String[] imageNames2 = req.getParameterValues("image_name_2[]");
        if (imageNames2 != null) {
            for (int i = 0; i < imageNames2.length; i++) {
                Part filePart = req.getPart("image_file_2_" + i);
                if (filePart != null && filePart.getSize() > 0) {
                    AsfDocumentImage image = new AsfDocumentImage(
                            "2",
                            filePart.getInputStream().readAllBytes(),
                            imageNames2[i]
                    );
                    asfDocumentImageService.save(image, asfId);
                }
            }
        }
    }

    private String getParamValue(HttpServletRequest req, String paramName, int index) {
        String[] values = req.getParameterValues(paramName);
        return values != null && values.length > index ? values[index] : null;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}