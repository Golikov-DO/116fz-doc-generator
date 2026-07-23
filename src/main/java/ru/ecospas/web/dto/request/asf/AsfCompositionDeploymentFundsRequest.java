package ru.ecospas.web.dto.request.asf;

public record AsfCompositionDeploymentFundsRequest(

        String responsibilityArea,
        String deploymentPlace,
        String dutyOfficerTelephone,
        String contactTelephone,
        String eMail,
        String numberBuildings,
        String totalArea
) {
}