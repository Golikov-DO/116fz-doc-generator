package ru.ecospas.web.mapper.asf;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.web.dto.request.asf.*;

import java.util.Collections;
import java.util.List;

@Component
public class AsfRequestMapper {
    public void toAsf(SaveAsfRequest request, Asf asf) {
        asf.setFullName(request.fullName());
        asf.setFullNameGen(request.fullNameGen());
        asf.setShortName(request.shortName());
        asf.setStatusShort(request.statusShort());
    }

    public void toCertificate(AsfCertificateRequest request, AsfCertificate certificate) {
        if (request == null) {
            return;
        }
        certificate.setCertNumber(request.certNumber());
        certificate.setCertSeries(request.certSeries());
        certificate.setIssuedBy(request.issuedBy());
        certificate.setIssueBasis(request.issueBasis());
        certificate.setIssueDate(request.issueDate());
        certificate.setValidUntil(request.validUntil());
    }

    public void toPersonnel(AsfPersonnelRequest request, AsfPersonnel personnel) {
        if (request == null) {
            return;
        }
        personnel.setStaffByStaffing(request.staffByStaffing());
        personnel.setStaffByList(request.staffByList());
        personnel.setCertifiedTotal(request.certifiedTotal());
        personnel.setQualifiedTotal(request.qualifiedTotal());
        personnel.setFirstClass(request.firstClass());
        personnel.setSecondClass(request.secondClass());
        personnel.setThirdClass(request.thirdClass());
        personnel.setInternationalClass(request.internationalClass());
    }

    public void toSpecialists(AsfSpecialistsRequest request, AsfSpecialists specialists) {
        if (request == null) {
            return;
        }
        specialists.setTotalCount(request.totalCount());
        specialists.setAsrTp(request.asrTp());
        specialists.setAsrLrnTer(request.asrLrnTer());
        specialists.setGzsr(request.gzsr());
        specialists.setPsr(request.psr());
        specialists.setDriver(request.driver());
        specialists.setAsrLrnSea(request.asrLrnSea());
    }

    public void toDeployment(
            AsfCompositionDeploymentFundsRequest request,
            AsfCompositionDeploymentFunds deployment
    ) {
        if (request == null) {
            return;
        }
        deployment.setResponsibilityArea(request.responsibilityArea());
        deployment.setDeploymentPlace(request.deploymentPlace());
        deployment.setDutyOfficerTelephone(request.dutyOfficerTelephone());
        deployment.setContactTelephone(request.contactTelephone());
        deployment.setEMail(request.eMail());
        deployment.setNumberBuildings(request.numberBuildings());
        deployment.setTotalArea(request.totalArea());
    }

    public List<AsfSigner> toSigners(List<AsfSignerRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream().map(this::toSigner).toList();
    }

    private AsfSigner toSigner(AsfSignerRequest request) {
        AsfSigner signer = new AsfSigner();
        signer.setId(request.id());
        signer.setName(request.name());
        signer.setPosition(request.position());
        return signer;
    }

    public List<AsfWorkType> toWorkTypes(List<AsfWorkTypeRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream().map(this::toWorkType).toList();
    }

    private AsfWorkType toWorkType(AsfWorkTypeRequest request) {
        AsfWorkType type = new AsfWorkType();
        type.setId(request.id());
        type.setName(request.name());
        return type;
    }

    public List<AsfDocumentImage> toImages(List<AsfImageRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream().map(this::toImage).toList();
    }

    private AsfDocumentImage toImage(AsfImageRequest request) {
        AsfDocumentImage image = new AsfDocumentImage();
        image.setId(request.id());
        image.setGroupKey(request.groupKey());
        image.setNameDocument(request.nameDocument());
        return image;
    }
}