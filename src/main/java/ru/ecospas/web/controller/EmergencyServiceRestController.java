package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ReferenceEmergencyServices;
import ru.ecospas.domain.service.ReferenceEmergencyServicesService;
import ru.ecospas.web.dto.request.emergency.SaveEmergencyServiceRequest;
import ru.ecospas.web.dto.response.emergency.EmergencyServiceListResponse;
import ru.ecospas.web.dto.response.emergency.EmergencyServiceResponse;
import ru.ecospas.web.mapper.emergency.EmergencyServiceResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-services")
@RequiredArgsConstructor
public class EmergencyServiceRestController {

    private final ReferenceEmergencyServicesService emergencyService;
    private final EmergencyServiceResponseMapper responseMapper;

    @GetMapping
    public List<EmergencyServiceListResponse> getEmergencyServices() {
        return responseMapper.toListResponses(emergencyService.findAll());
    }

    @GetMapping("/{id}")
    public EmergencyServiceResponse getEmergencyService(@PathVariable Integer id) {
        ReferenceEmergencyServices service = emergencyService.loadRest(id);
        if (service == null) {
            throw new IllegalArgumentException("Emergency service not found");
        }
        return responseMapper.toResponse(service);
    }

    @PostMapping
    public EmergencyServiceResponse createEmergencyService(
            @Valid
            @RequestBody
            SaveEmergencyServiceRequest request
    ) {
        return responseMapper.toResponse(emergencyService.create(request));
    }

    @PutMapping("/{id}")
    public EmergencyServiceResponse updateEmergencyService(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveEmergencyServiceRequest request
    ) {
        ReferenceEmergencyServices service = emergencyService.update(id, request);
        if (service == null) {
            throw new IllegalArgumentException("Emergency service not found");
        }
        return responseMapper.toResponse(service);
    }

    @DeleteMapping("/{id}")
    public void deleteEmergencyService(@PathVariable Integer id) {
        emergencyService.deleteRest(id);
    }
}