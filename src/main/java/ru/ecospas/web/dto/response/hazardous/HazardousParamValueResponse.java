package ru.ecospas.web.dto.response.hazardous;

public record HazardousParamValueResponse(

        Integer paramId,
        String sectionNo,
        String title,
        String valueText,
        String sourceInfo

) {
}