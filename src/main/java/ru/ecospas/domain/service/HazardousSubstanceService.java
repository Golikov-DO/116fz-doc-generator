package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectHazardousParamValue;
import ru.ecospas.domain.model.ReferenceHazardousParam;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.repository.ObjectHazardousParamValueRepository;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.ReferenceHazardousParamRepository;
import ru.ecospas.domain.repository.ReferenceHazardousSubstanceRepository;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;

@Service
@RequiredArgsConstructor
@Transactional
public class HazardousSubstanceService {

    private final ObjectModelRepository objectModelRepository;
    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final ReferenceHazardousParamRepository paramRepository;
    private final ObjectHazardousParamValueRepository valueRepository;

    public ReferenceHazardousSubstance load(Integer id) {
        return objectModelRepository.findById(id).get().getHazardousSubstance();
    }

    public ReferenceHazardousSubstance create() {

        ReferenceHazardousSubstance substance =
                new ReferenceHazardousSubstance();

        substance.setName("");
        substance.setNameGen("");

        return substance;
    }

    public ReferenceHazardousSubstance save(
            HttpServletRequest req,
            ReferenceHazardousSubstance substance
    ) {

        substance.setName(
                param(req, "name")
        );

        substance.setNameGen(
                param(req, "nameGen")
        );

        substance = substanceRepository.save(substance);

        saveValues(req, substance);

        return substance;
    }

    private void saveValues(
            HttpServletRequest req,
            ReferenceHazardousSubstance substance
    ) {

        List<ReferenceHazardousParam> params =
                paramRepository.findAll();

        List<ObjectHazardousParamValue> existing =
                valueRepository.findAllBySubstanceId(substance.getId());

        for (ReferenceHazardousParam param : params) {

            String value =
                    req.getParameter("value_" + param.getId());

            String source =
                    req.getParameter("source_" + param.getId());

            ObjectHazardousParamValue entity =
                    existing.stream()
                            .filter(v ->
                                    v.getParam().getId().equals(param.getId()))
                            .findFirst()
                            .orElseGet(() -> {
                                ObjectHazardousParamValue e =
                                        new ObjectHazardousParamValue();
                                e.setParam(param);
                                e.setSubstance(substance);
                                return e;
                            });

            entity.setValueText(value);
            entity.setSourceInfo(source);

            valueRepository.save(entity);
        }
    }

    public ReferenceHazardousSubstance createEmpty() {

        return substanceRepository.save(
                create()
        );
    }

    public void delete(Integer id) {

        List<ObjectHazardousParamValue> values =
                valueRepository.findAllBySubstanceId(id);

        for (ObjectHazardousParamValue value : values) {
            valueRepository.deleteById(value.getId());
        }

        substanceRepository.deleteById(id);
    }

    public List<ReferenceHazardousParam> loadParams() {
        return paramRepository.findAll();
    }

    public List<ObjectHazardousParamValue> loadValues(
            Integer substanceId
    ) {
        return valueRepository.findAllBySubstanceId(substanceId);
    }
}