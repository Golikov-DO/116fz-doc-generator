package ru.ecospas.web.dto.request.asf;

import java.util.List;

public record SaveAsfRequest(

        String fullName,
        String fullNameGen,
        String shortName,
        String statusShort,

        AsfCertificateRequest certificate,
        AsfPersonnelRequest personnel,
        AsfSpecialistsRequest specialists,
        AsfCompositionDeploymentFundsRequest deployment,
        List<AsfSignerRequest> signers,
        List<AsfWorkTypeRequest> workTypes,
        List<AsfImageRequest> images
) {
}