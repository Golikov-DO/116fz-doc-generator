package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;
import ru.ecospas.domain.repository.ReferenceHazardousSubstanceRepository;
import ru.ecospas.web.dto.request.hazardous.SaveHazardousSubstanceRequest;
import ru.ecospas.web.mapper.hazardous.HazardousSubstanceRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HazardousSubstanceService {

    private final ReferenceHazardousSubstanceRepository substanceRepository;
    private final HazardousSubstanceRequestMapper requestMapper;

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