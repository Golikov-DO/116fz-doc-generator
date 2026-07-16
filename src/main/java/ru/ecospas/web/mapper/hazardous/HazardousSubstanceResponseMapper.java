package ru.ecospas.web.mapper.hazardous;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.web.dto.response.hazardous.HazardousParamValueResponse;
import ru.ecospas.web.dto.response.hazardous.HazardousSubstanceListResponse;
import ru.ecospas.web.dto.response.hazardous.HazardousSubstanceResponse;

import java.util.Collections;
import java.util.List;

@Component
public class HazardousSubstanceResponseMapper {

    public HazardousSubstanceResponse toResponse(ReferenceHazardousSubstance substance) {
        if (substance == null) {
            return null;
        }
        return new HazardousSubstanceResponse(
                substance.getId(),
                substance.getName(),
                substance.getNameGen(),
                toValues(substance.getValues())
        );
    }

    public List<HazardousSubstanceResponse> toResponses(
            List<ReferenceHazardousSubstance> list
    ) {
        return list.stream().map(this::toResponse).toList();
    }

    public HazardousSubstanceListResponse toListResponse(ReferenceHazardousSubstance substance) {
        if (substance == null) {
            return null;
        }
        return new HazardousSubstanceListResponse(substance.getId(),substance.getName());
    }

    public List<HazardousSubstanceListResponse> toListResponses(
            List<ReferenceHazardousSubstance> list
    ) {

        return list.stream().map(this::toListResponse).toList();
    }

    private List<HazardousParamValueResponse> toValues(
            List<SubstanceHazardousParamValue> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(value -> new HazardousParamValueResponse(
                        value.getParam().getId(),
                        value.getParam().getSectionNo(),
                        value.getParam().getTitle(),
                        value.getValueText(),
                        value.getSourceInfo()
                ))
                .toList();
    }
}