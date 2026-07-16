package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.domain.repository.ScenarioRepository;
import ru.ecospas.web.dto.request.scenario.SaveScenarioRequest;
import ru.ecospas.web.mapper.scenario.ScenarioRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScenarioService {

    private final ScenarioRepository repository;
    private final ScenarioRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public List<Scenario> findAll() {

        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Scenario loadRest(Integer id) {

        return repository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Scenario create(SaveScenarioRequest request) {
        Scenario scenario = new Scenario();
        return save(request, scenario);
    }

    @Transactional
    public Scenario save(SaveScenarioRequest request, Scenario scenario) {
        requestMapper.toScenario(request, scenario);
        return repository.save(scenario);
    }

    @Transactional
    public Scenario update(Integer id, SaveScenarioRequest request) {
        Scenario scenario = loadRest(id);
        if (scenario == null) {
            return null;
        }
        return save(request, scenario);
    }

    @Transactional
    public void deleteRest(Integer id) {
        Scenario scenario = loadRest(id);
        if (scenario == null) {
            return;
        }
        repository.delete(scenario);
    }
}