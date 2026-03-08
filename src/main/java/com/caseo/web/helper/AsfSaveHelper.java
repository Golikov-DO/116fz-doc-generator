package com.caseo.web.helper;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class AsfSaveHelper {

    public Asf createAsf(HttpServletRequest req) {
        Asf asf = new Asf();
        asf.setFullName(req.getParameter("full_name"));
        asf.setFullNameGen(req.getParameter("full_name_gen"));
        asf.setShortName(req.getParameter("short_name"));
        asf.setEmail(req.getParameter("email"));
        asf.setStatusShort(req.getParameter("status_short"));

        // Обработка времени прибытия
        String hours = req.getParameter("arrival_hours");
        String minutes = req.getParameter("arrival_minutes");
        if (hours != null && !hours.isEmpty()) {
            String arrivalTime = minutes != null && !minutes.isEmpty()
                    ? String.format("%s:%s:00", hours, minutes)
                    : String.format("%s:00:00", hours);
            asf.setArrivalTime(arrivalTime);
        }
        return asf;
    }

    public void addCertificate(HttpServletRequest req, Asf asf) {
        String certNumber = req.getParameter("cert_number");
        if (certNumber != null && !certNumber.isEmpty()) {
            AsfCertificate cert = new AsfCertificate();
            cert.setCertNumber(certNumber);
            cert.setCertSeries(req.getParameter("cert_series"));
            cert.setIssuedBy(req.getParameter("issued_by"));
            cert.setIssueBasis(req.getParameter("issue_basis"));
            cert.setIssueDate(req.getParameter("issue_date"));
            cert.setValidUntil(req.getParameter("valid_until"));
            cert.setAsf(asf);
        }
    }

    public void addPersonnel(HttpServletRequest req, Asf asf) {
        String staffByStaffing = req.getParameter("staff_by_staffing");
        if (staffByStaffing != null && !staffByStaffing.isEmpty()) {
            AsfPersonnel personnel = new AsfPersonnel();
            personnel.setStaffByStaffing(Integer.parseInt(staffByStaffing));
            personnel.setStaffByList(Integer.parseInt(req.getParameter("staff_by_list")));
            personnel.setCertifiedTotal(Integer.parseInt(req.getParameter("certified_total")));
            personnel.setQualifiedTotal(Integer.parseInt(req.getParameter("qualified_total")));
            personnel.setThirdClass(Integer.parseInt(req.getParameter("third_class")));
            personnel.setSecondClass(Integer.parseInt(req.getParameter("second_class")));
            personnel.setFirstClass(Integer.parseInt(req.getParameter("first_class")));
            personnel.setInternationalClass(Integer.parseInt(req.getParameter("international_class")));
            personnel.setAsf(asf);
        }
    }

    public void addSpecialists(HttpServletRequest req, Asf asf) {
        String totalCount = req.getParameter("specialists_total");
        if (totalCount != null && !totalCount.isEmpty()) {
            AsfSpecialists specialists = new AsfSpecialists();
            specialists.setTotalCount(Integer.parseInt(totalCount));
            specialists.setAsrTp(Integer.parseInt(req.getParameter("asr_tp")));
            specialists.setAsrLrnTer(Integer.parseInt(req.getParameter("asr_lrn_ter")));
            specialists.setGzsr(Integer.parseInt(req.getParameter("gzsr")));
            specialists.setPsr(Integer.parseInt(req.getParameter("psr")));
            specialists.setDriver(Integer.parseInt(req.getParameter("driver")));
            specialists.setAsrLrnSea(Integer.parseInt(req.getParameter("asr_lrn_sea")));
            specialists.setAsf(asf);
        }
    }

    public void addDeployment(HttpServletRequest req, Asf asf) {
        String responsibilityArea = req.getParameter("responsibility_area[]");
        if (responsibilityArea != null && !responsibilityArea.isEmpty()) {
            AsfCompositionDeploymentFunds deployment = new AsfCompositionDeploymentFunds();
            deployment.setResponsibilityArea(responsibilityArea);
            deployment.setDeploymentPlace(req.getParameter("deployment_place[]"));
            deployment.setDutyOfficerTelephone(req.getParameter("duty_officer_phone[]"));
            deployment.setContactTelephone(req.getParameter("contact_phone[]"));
            deployment.setEMail(req.getParameter("deployment_email[]"));
            deployment.setNumberBuildings(req.getParameter("number_buildings[]"));
            deployment.setTotalArea(req.getParameter("total_area[]"));
            deployment.setAsf(asf);
        }
    }

    public void addSigners(HttpServletRequest req, Asf asf) {
        String[] signerNames = req.getParameterValues("signer_name[]");
        String[] signerPositions = req.getParameterValues("signer_position[]");

        if (signerNames != null) {
            for (int i = 0; i < signerNames.length; i++) {
                if (signerNames[i] != null && !signerNames[i].trim().isEmpty()) {
                    AsfSigner signer = new AsfSigner();
                    signer.setName(signerNames[i]);
                    signer.setPosition(signerPositions != null && i < signerPositions.length ? signerPositions[i] : null);
                    signer.setAsf(asf);
                }
            }
        }
    }

    public void addWorkTypes(HttpServletRequest req, Asf asf) {
        String[] workTypeNames = req.getParameterValues("work_type_name[]");
        if (workTypeNames != null) {
            for (String workTypeName : workTypeNames) {
                if (workTypeName != null && !workTypeName.trim().isEmpty()) {
                    AsfWorkType workType = new AsfWorkType();
                    workType.setName(workTypeName.trim());
                    workType.setAsf(asf);
                }
            }
        }
    }

    public void addImages(HttpServletRequest req, Asf asf) {
        // Приложение 1
        String[] imageNames1 = req.getParameterValues("image_name_1[]");
        if (imageNames1 != null) {
            for (int i = 0; i < imageNames1.length; i++) {
                try {
                    Part filePart = req.getPart("image_file_1_" + i);
                    if (filePart != null && filePart.getSize() > 0) {
                        AsfDocumentImage image = new AsfDocumentImage();
                        image.setGroupKey("1");
                        image.setNameDocument(imageNames1[i]);
                        image.setImageBlob(filePart.getInputStream().readAllBytes());
                        image.setAsf(asf);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // логируй нормально
                }
            }
        }

        // Приложение 2
        String[] imageNames2 = req.getParameterValues("image_name_2[]");
        if (imageNames2 != null) {
            for (int i = 0; i < imageNames2.length; i++) {
                try {
                    Part filePart = req.getPart("image_file_2_" + i);
                    if (filePart != null && filePart.getSize() > 0) {
                        AsfDocumentImage image = new AsfDocumentImage();
                        image.setGroupKey("2");
                        image.setNameDocument(imageNames2[i]);
                        image.setImageBlob(filePart.getInputStream().readAllBytes());
                        image.setAsf(asf);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // логируй нормально
                }
            }
        }
    }
}