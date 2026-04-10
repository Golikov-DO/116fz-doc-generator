package ru.ecospas.web.helper;

import jakarta.servlet.http.HttpServletRequest;
import ru.ecospas.domain.model.*;
import ru.ecospas.web.util.MapListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.*;

public class AsfSaveHelper {

    // ASF
    public void mapAsf(HttpServletRequest req, Asf asf) {

        asf.setFullName(param(req, "full_name"));
        asf.setFullNameGen(param(req, "full_name_gen"));
        asf.setShortName(param(req, "short_name"));
        asf.setStatusShort(param(req, "status_short"));

        asf.setArrivalTime(paramTime(req, "arrival_hours", "arrival_minutes"));
    }

    // Asf certificate
    public void mapCertificate(HttpServletRequest req, AsfCertificate cert) {
        cert.setCertNumber(param(req, "cert_number"));
        cert.setCertSeries(param(req, "cert_series"));
        cert.setIssuedBy(param(req, "issued_by"));
        cert.setIssueBasis(param(req, "issue_basis"));
        cert.setIssueDate(paramDate(req, "issue_date"));
        cert.setValidUntil(paramDate(req, "valid_until"));
    }

    // The quality of the specialists
    public void mapPersonnel(HttpServletRequest req, AsfPersonnel personnel) {
        personnel.setStaffByStaffing(paramInt(req, "staff_by_staffing"));
        personnel.setStaffByList(paramInt(req, "staff_by_list"));
        personnel.setCertifiedTotal(paramInt(req, "certified_total"));
        personnel.setQualifiedTotal(paramInt(req, "qualified_total"));
        personnel.setThirdClass(paramInt(req, "third_class"));
        personnel.setSecondClass(paramInt(req, "second_class"));
        personnel.setFirstClass(paramInt(req, "first_class"));
        personnel.setInternationalClass(paramInt(req, "international_class"));
    }

    // СSpecialists
    public void mapSpecialists(HttpServletRequest req, AsfSpecialists specialists) {
        specialists.setTotalCount(paramInt(req, "specialists_total"));
        specialists.setAsrTp(paramInt(req, "asr_tp"));
        specialists.setAsrLrnTer(paramInt(req, "asr_lrn_ter"));
        specialists.setGzsr(paramInt(req, "gzsr"));
        specialists.setPsr(paramInt(req, "psr"));
        specialists.setDriver(paramInt(req, "driver"));
        specialists.setAsrLrnSea(paramInt(req, "asr_lrn_sea"));
    }

    // Additional information on ASF
    public void mapDeployment(HttpServletRequest req, AsfCompositionDeploymentFunds deployment) {
        deployment.setResponsibilityArea(param(req, "responsibility_area"));
        deployment.setDeploymentPlace(param(req, "deployment_place"));
        deployment.setDutyOfficerTelephone(param(req, "duty_officer_phone"));
        deployment.setContactTelephone(param(req, "contact_phone"));
        deployment.setEMail(param(req, "deployment_email"));
        deployment.setNumberBuildings(param(req, "number_buildings"));
        deployment.setTotalArea(param(req, "total_area"));
    }

    // Asf signers
    public void mapSigner(HttpServletRequest req, int index, AsfSigner signer) {
        signer.setName(param(req, "signer_name[]", index));
        signer.setPosition(param(req, "signer_position[]", index));
    }

    public List<AsfSigner> mapSigners(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "signer_id[]",
                AsfSigner::new,
                (ctx, signer) -> mapSigner(ctx.req, ctx.index, signer)
        );
    }

    // Type of work according to the certificate
    public void mapWorkType(HttpServletRequest req, int index, AsfWorkType workType) {
        workType.setName(param(req, "work_type_name[]", index));
    }

    public List<AsfWorkType> mapWorkTypes(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "work_type_id[]",
                AsfWorkType::new,
                (ctx, type) -> mapWorkType(ctx.req, ctx.index, type)
        );
    }
}