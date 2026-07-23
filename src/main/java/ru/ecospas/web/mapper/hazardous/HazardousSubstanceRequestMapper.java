package ru.ecospas.web.mapper.hazardous;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.model.SubstanceHazardousParam;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.repository.ReferenceHazardousParamRepository;
import ru.ecospas.web.dto.request.hazardous.HazardousParamValueRequest;
import ru.ecospas.web.dto.request.hazardous.SaveHazardousSubstanceRequest;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HazardousSubstanceRequestMapper {

    private final ReferenceHazardousParamRepository paramRepository;

    public void toSubstance(
            SaveHazardousSubstanceRequest request,
            ReferenceHazardousSubstance substance
    ) {
        substance.setName(request.name());
        substance.setNameShort(request.nameShort());
        Map<Integer, SubstanceHazardousParamValue> existing = new HashMap<>();
        for (SubstanceHazardousParamValue value : substance.getValues()) {
            existing.put(value.getParam().getId(), value);
        }
        substance.getValues().clear();
        if (request.values() == null) {
            return;
        }
        for (HazardousParamValueRequest dto : request.values()) {
            SubstanceHazardousParamValue value = existing.get(dto.paramId());
            if (value == null) {
                value = new SubstanceHazardousParamValue();
                value.setSubstance(substance);
                SubstanceHazardousParam param = paramRepository.findById(dto.paramId())
                                .orElseThrow();
                value.setParam(param);
            }
            value.setValueText(dto.valueText());
            value.setSourceInfo(dto.sourceInfo());
            substance.getValues().add(value);
        }
    }
}