package ru.ecospas.web.mapper.asf;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.web.dto.request.asf.AsfListResponse;
import ru.ecospas.web.dto.response.asf.*;

import java.util.Collections;
import java.util.List;

@Component
public class AsfResponseMapper {
    public AsfResponse toResponse(Asf asf) {

        if (asf == null) {
            return null;
        }

        return new AsfResponse(

                asf.getId(),

                asf.getFullName(),
                asf.getFullNameGen(),
                asf.getShortName(),
                asf.getStatusShort(),

                toCertificate(asf.getCertificate()),
                toPersonnel(asf.getPersonnel()),
                toSpecialists(asf.getSpecialists()),
                toDeployment(asf.getDeployment()),
                toSigners(asf.getSigners()),
                toWorkTypes(asf.getWorkTypes()),
                toImages(asf.getImages())
        );
    }

    public List<AsfResponse> toResponses(List<Asf> asfs) {
        return asfs.stream()
                .map(this::toResponse)
                .toList();
    }

    public AsfListResponse toListResponse(Asf asf) {

        if (asf == null) {
            return null;
        }

        return new AsfListResponse(
                asf.getId(),
                asf.getShortName()
        );
    }

    public List<AsfListResponse> toListResponses(List<Asf> asfs) {

        return asfs.stream()
                .map(this::toListResponse)
                .toList();
    }

    private AsfCertificateResponse toCertificate(
            AsfCertificate certificate
    ) {

        if (certificate == null) {
            return null;
        }

        return new AsfCertificateResponse(

                certificate.getCertNumber(),
                certificate.getCertSeries(),
                certificate.getIssuedBy(),
                certificate.getIssueBasis(),

                certificate.getIssueDate(),
                certificate.getValidUntil()
        );
    }

    private AsfPersonnelResponse toPersonnel(
            AsfPersonnel personnel
    ) {

        if (personnel == null) {
            return null;
        }

        return new AsfPersonnelResponse(

                personnel.getStaffByStaffing(),
                personnel.getStaffByList(),
                personnel.getCertifiedTotal(),
                personnel.getQualifiedTotal(),

                personnel.getFirstClass(),
                personnel.getSecondClass(),
                personnel.getThirdClass(),
                personnel.getInternationalClass()
        );
    }

    private AsfSpecialistsResponse toSpecialists(AsfSpecialists specialists) {
        if (specialists == null) {
            return null;
        }
        return new AsfSpecialistsResponse(
                specialists.getTotalCount(),
                specialists.getAsrTp(),
                specialists.getAsrLrnTer(),
                specialists.getGzsr(),
                specialists.getPsr(),
                specialists.getDriver(),
                specialists.getAsrLrnSea()
        );
    }

    private AsfCompositionDeploymentFundsResponse toDeployment(
            AsfCompositionDeploymentFunds deployment
    ) {
        if (deployment == null) {
            return null;
        }
        return new AsfCompositionDeploymentFundsResponse(
                deployment.getResponsibilityArea(),
                deployment.getDeploymentPlace(),
                deployment.getDutyOfficerTelephone(),
                deployment.getContactTelephone(),
                deployment.getEMail(),
                deployment.getNumberBuildings(),
                deployment.getTotalArea()
        );
    }

    private List<AsfSignerResponse> toSigners(
            List<AsfSigner> signers
    ) {

        if (signers == null) {
            return Collections.emptyList();
        }

        return signers.stream()
                .map(item -> new AsfSignerResponse(
                        item.getId(),
                        item.getName(),
                        item.getPosition()
                ))
                .toList();
    }

    private List<AsfWorkTypeResponse> toWorkTypes(
            List<AsfWorkType> workTypes
    ) {

        if (workTypes == null) {
            return Collections.emptyList();
        }

        return workTypes.stream()
                .map(item -> new AsfWorkTypeResponse(
                        item.getId(),
                        item.getName()
                ))
                .toList();
    }

    private List<AsfImageResponse> toImages(
            List<AsfDocumentImage> images
    ) {

        if (images == null) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(item -> new AsfImageResponse(
                        item.getId(),
                        item.getGroupKey(),
                        item.getNameDocument()
                ))
                .toList();
    }
}