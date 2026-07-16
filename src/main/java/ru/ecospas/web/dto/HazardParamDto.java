package ru.ecospas.web.dto;

import lombok.Getter;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.model.SubstanceHazardousParam;

@Getter
public class HazardParamDto {

    private final Integer paramId;
    private final String section;
    private final String title;
    private final String value;
    private final String source;

    public HazardParamDto(
            SubstanceHazardousParam param,
            SubstanceHazardousParamValue valueObj
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