package ru.ecospas.web.dto.response.asf;

import java.util.List;

public record AsfResponse(

        Integer id,

        String fullName,
        String fullNameGen,
        String shortName,
        String statusShort,

        AsfCertificateResponse certificate,

        AsfPersonnelResponse personnel,

        AsfSpecialistsResponse specialists,

        AsfCompositionDeploymentFundsResponse deployment,

        List<AsfSignerResponse> signers,

        List<AsfWorkTypeResponse> workTypes,

        List<AsfImageResponse> images
) {
}