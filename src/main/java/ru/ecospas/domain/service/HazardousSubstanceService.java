package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.model.SubstanceHazardousParam;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.repository.ReferenceHazardousParamValueRepository;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.ReferenceHazardousParamRepository;
import ru.ecospas.domain.repository.ReferenceHazardousSubstanceRepository;
import ru.ecospas.web.dto.request.hazardous.SaveHazardousSubstanceRequest;
import ru.ecospas.web.mapper.hazardous.HazardousSubstanceRequestMapper;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;

@Service
@RequiredArgsConstructor
@Transactional
public class HazardousSubstanceService {

    private final ObjectModelRepository objectModelRepository;
    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final ReferenceHazardousParamRepository paramRepository;
    private final ReferenceHazardousParamValueRepository valueRepository;

    private final HazardousSubstanceRequestMapper requestMapper;

    public ReferenceHazardousSubstance load(Integer id) {
        return objectModelRepository.findById(id).get().getHazardousSubstance();
    }

    public ReferenceHazardousSubstance create() {
        ReferenceHazardousSubstance substance = new ReferenceHazardousSubstance();
        substance.setName("");
        substance.setNameGen("");
        return substance;
    }

    public ReferenceHazardousSubstance save(HttpServletRequest req,
            ReferenceHazardousSubstance substance
    ) {
        substance.setName(param(req, "name"));
        substance.setNameGen(param(req, "nameGen"));
        substance = substanceRepository.save(substance);
        saveValues(req, substance);
        return substance;
    }

    private void saveValues(HttpServletRequest req, ReferenceHazardousSubstance substance) {
        List<SubstanceHazardousParam> params = paramRepository.findAll();
        List<SubstanceHazardousParamValue> existing =
                valueRepository.findAllBySubstanceId(substance.getId());
        for (SubstanceHazardousParam param : params) {
            String value = req.getParameter("value_" + param.getId());
            String source = req.getParameter("source_" + param.getId());
            SubstanceHazardousParamValue entity =
                    existing.stream()
                            .filter(v ->
                                    v.getParam().getId().equals(param.getId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SubstanceHazardousParamValue e =
                                        new SubstanceHazardousParamValue();
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
        List<SubstanceHazardousParamValue> values = valueRepository.findAllBySubstanceId(id);
        for (SubstanceHazardousParamValue value : values) {
            valueRepository.deleteById(value.getId());
        }
        substanceRepository.deleteById(id);
    }

    public List<SubstanceHazardousParam> loadParams() {
        return paramRepository.findAll();
    }

    public List<SubstanceHazardousParamValue> loadValues(Integer substanceId) {
        return valueRepository.findAllBySubstanceId(substanceId);
    }

    //REST
    @Transactional(readOnly = true)
    public ReferenceHazardousSubstance loadRest(Integer id) {
        ReferenceHazardousSubstance substance = substanceRepository.findById(id)
                        .orElse(null);
        if (substance == null) {
            return null;
        }
        Hibernate.initialize(substance.getValues());
        for (SubstanceHazardousParamValue value : substance.getValues()) {
            Hibernate.initialize(value.getParam());
        }
        return substance;
    }

    @Transactional(readOnly = true)
    public List<ReferenceHazardousSubstance> findAll() {
        return substanceRepository.findAll();
    }

    @Transactional
    public ReferenceHazardousSubstance create(SaveHazardousSubstanceRequest request) {
        ReferenceHazardousSubstance substance = new ReferenceHazardousSubstance();
        return save(request, substance);
    }

    @Transactional
    public ReferenceHazardousSubstance save(SaveHazardousSubstanceRequest request,
            ReferenceHazardousSubstance substance
    ) {
        requestMapper.toSubstance(request, substance);
        return substanceRepository.save(substance);
    }

    @Transactional
    public ReferenceHazardousSubstance update(Integer id, SaveHazardousSubstanceRequest request) {
        ReferenceHazardousSubstance substance = loadRest(id);
        if (substance == null) {
            return null;
        }
        return save(request, substance);
    }

    @Transactional
    public void deleteRest(Integer id) {
        ReferenceHazardousSubstance substance = loadRest(id);
        if (substance == null) {
            return;
        }
        substanceRepository.delete(substance);
    }
}