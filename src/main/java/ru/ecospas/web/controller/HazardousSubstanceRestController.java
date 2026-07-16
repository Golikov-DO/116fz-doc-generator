package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;
import ru.ecospas.domain.service.HazardousSubstanceService;
import ru.ecospas.web.dto.request.hazardous.SaveHazardousSubstanceRequest;
import ru.ecospas.web.dto.response.hazardous.HazardousSubstanceListResponse;
import ru.ecospas.web.dto.response.hazardous.HazardousSubstanceResponse;
import ru.ecospas.web.mapper.hazardous.HazardousSubstanceResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/hazardous-substances")
@RequiredArgsConstructor
public class HazardousSubstanceRestController {

    private final HazardousSubstanceService hazardousSubstanceService;
    private final HazardousSubstanceResponseMapper responseMapper;

    @GetMapping
    public List<HazardousSubstanceListResponse> getHazardousSubstances() {
        return responseMapper.toListResponses(
                hazardousSubstanceService.findAll()
        );
    }

    @GetMapping("/{id}")
    public HazardousSubstanceResponse getHazardousSubstance(@PathVariable Integer id) {
        ReferenceHazardousSubstance substance = hazardousSubstanceService.loadRest(id);
        if (substance == null) {
            throw new IllegalArgumentException(
                    "Hazardous substance not found"
            );
        }
        return responseMapper.toResponse(substance);
    }

    @PostMapping
    public HazardousSubstanceResponse createHazardousSubstance(
            @Valid
            @RequestBody
            SaveHazardousSubstanceRequest request
    ) {
        ReferenceHazardousSubstance substance = hazardousSubstanceService.create(request);
        return responseMapper.toResponse(substance);
    }

    @PutMapping("/{id}")
    public HazardousSubstanceResponse updateHazardousSubstance(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveHazardousSubstanceRequest request
    ) {
        ReferenceHazardousSubstance substance = hazardousSubstanceService.update(id, request);
        if (substance == null) {
            throw new IllegalArgumentException(
                    "Hazardous substance not found"
            );
        }
        return responseMapper.toResponse(substance);
    }

    @DeleteMapping("/{id}")
    public void deleteHazardousSubstance(@PathVariable Integer id) {
        hazardousSubstanceService.deleteRest(id);
    }
}