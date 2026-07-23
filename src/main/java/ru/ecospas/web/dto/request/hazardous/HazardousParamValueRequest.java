package ru.ecospas.web.dto.request.hazardous;

public record HazardousParamValueRequest(

        Integer paramId,
        String valueText,
        String sourceInfo

) {
}