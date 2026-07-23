package ru.ecospas.web.dto.response.asf;

public record AsfCompositionDeploymentFundsResponse(

        String responsibilityArea,
        String deploymentPlace,
        String dutyOfficerTelephone,
        String contactTelephone,
        String eMail,
        String numberBuildings,
        String totalArea
) {
}