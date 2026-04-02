package com.caseo.web.dto;

import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.model.ReferenceHazardousParam;
import lombok.Getter;

@Getter
public class HazardParamDto {

    private final Integer paramId;
    private final String section;
    private final String title;
    private final String value;
    private final String source;

    public HazardParamDto(
            ReferenceHazardousParam param,
            ObjectHazardousParamValue valueObj
    ) {
        this.paramId = param.getId();
        this.section = param.getSectionNo();
        this.title = param.getTitle();

        this.value = (valueObj != null && valueObj.getValueText() != null)
                ? valueObj.getValueText()
                : "";

        this.source = (valueObj != null && valueObj.getSourceInfo() != null)
                ? valueObj.getSourceInfo()
                : "";
    }
}