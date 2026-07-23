package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ReferenceEmergencyServices;
import ru.ecospas.domain.repository.ReferenceEmergencyServicesRepository;
import ru.ecospas.web.dto.request.emergency.SaveEmergencyServiceRequest;
import ru.ecospas.web.mapper.emergency.EmergencyServiceRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceEmergencyServicesService {

    private final ReferenceEmergencyServicesRepository repository;
    private final EmergencyServiceRequestMapper requestMapper;

    public List<ReferenceEmergencyServices> findAll() {

        return repository.findAll();
    }

    public ReferenceEmergencyServices loadRest(Integer id) {

        return repository.findById(id)
                .orElse(null);
    }

    @Transactional
    public ReferenceEmergencyServices create(SaveEmergencyServiceRequest request) {
        ReferenceEmergencyServices service = new ReferenceEmergencyServices();
        return save(request, service);
    }

    @Transactional
    public ReferenceEmergencyServices save(SaveEmergencyServiceRequest request,
                                           ReferenceEmergencyServices service) {
        requestMapper.toEmergencyService(request, service);
        return repository.save(service);
    }

    @Transactional
    public ReferenceEmergencyServices update(Integer id, SaveEmergencyServiceRequest request) {
        ReferenceEmergencyServices service = loadRest(id);
        if (service == null) {
            return null;
        }
        return save(request, service);
    }

    @Transactional
    public void deleteRest(Integer id) {
        ReferenceEmergencyServices service = loadRest(id);
        if (service == null) {
            return;
        }
        repository.delete(service);
    }
}