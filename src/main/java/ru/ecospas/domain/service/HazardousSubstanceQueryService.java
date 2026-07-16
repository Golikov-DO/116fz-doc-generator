package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.model.SubstanceHazardousParam;
import ru.ecospas.domain.repository.ReferenceHazardousParamValueRepository;
import ru.ecospas.domain.repository.ReferenceHazardousParamRepository;
import ru.ecospas.web.dto.HazardParamDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HazardousSubstanceQueryService {

    private final ReferenceHazardousParamRepository paramRepository;
    private final ReferenceHazardousParamValueRepository valueRepository;

    public List<HazardParamDto> getHazardParamsWithValues(
            Integer substanceId
    ) {

        List<SubstanceHazardousParam> params =
                paramRepository.findAll();

        Map<Integer, SubstanceHazardousParamValue> values =
                valueRepository.findAllBySubstanceId(substanceId)
                        .stream()
                        .collect(Collectors.toMap(
                                value -> value.getParam().getId(),
                                Function.identity()
                        ));

        return params.stream()
                .map(param -> new HazardParamDto(
                        param,
                        values.get(param.getId())
                ))
                .toList();
    }
}