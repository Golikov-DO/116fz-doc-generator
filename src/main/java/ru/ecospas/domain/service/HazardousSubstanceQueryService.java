package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.SubstanceHazardousParam;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.repository.ReferenceHazardousParamRepository;
import ru.ecospas.domain.repository.ReferenceHazardousParamValueRepository;
import ru.ecospas.web.dto.response.hazardous.HazardousParamResponse;
import ru.ecospas.web.dto.response.hazardous.HazardousParamValueResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HazardousSubstanceQueryService {

    private final ReferenceHazardousParamRepository paramRepository;
    private final ReferenceHazardousParamValueRepository valueRepository;

    public List<HazardousParamValueResponse> getHazardParamsWithValues(Integer substanceId) {
        List<SubstanceHazardousParam> params = paramRepository.findAll();

        Map<Integer, SubstanceHazardousParamValue> values =
                valueRepository.findAllBySubstanceId(substanceId)
                        .stream()
                        .collect(Collectors.toMap(
                                value -> value.getParam().getId(),
                                Function.identity()
                        ));
        return params.stream()
                .map(param -> {

                    SubstanceHazardousParamValue value =
                            values.get(param.getId());

                    return new HazardousParamValueResponse(
                            param.getId(),
                            param.getSectionNo(),
                            param.getTitle(),
                            value != null && value.getValueText() != null
                                    ? value.getValueText()
                                    : "",
                            value != null && value.getSourceInfo() != null
                                    ? value.getSourceInfo()
                                    : ""
                    );
                })
                .toList();
    }
    @Transactional(readOnly = true)
    public List<HazardousParamResponse> getAllParams() {
        return paramRepository.findAll().stream()
                .map(p -> new HazardousParamResponse(p.getId(), p.getSectionNo(), p.getTitle()))
                .toList();
    }

}