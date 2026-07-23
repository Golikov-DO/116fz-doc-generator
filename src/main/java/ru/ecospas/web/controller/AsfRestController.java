package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.service.AsfService;
import ru.ecospas.web.dto.request.asf.AsfListResponse;
import ru.ecospas.web.dto.request.asf.SaveAsfRequest;
import ru.ecospas.web.dto.response.asf.AsfResponse;
import ru.ecospas.web.mapper.asf.AsfResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/asfs")
@RequiredArgsConstructor
public class AsfRestController {

    private final AsfService asfService;
    private final AsfResponseMapper responseMapper;

    @GetMapping
    public List<AsfListResponse> getAsfs() {
        return responseMapper.toListResponses(
                asfService.findAll()
        );
    }

    @GetMapping("/{id}")
    public AsfResponse getAsf(@PathVariable Integer id) {
        Asf asf = asfService.loadRest(id);
        if (asf == null) {
            throw new IllegalArgumentException("ASF not found");
        }
        return responseMapper.toResponse(asf);
    }

    @PostMapping
    public AsfResponse createAsf(@Valid @RequestBody SaveAsfRequest request) {
        Asf asf = asfService.create(request);
        return responseMapper.toResponse(asf);
    }

    @PutMapping("/{id}")
    public AsfResponse updateAsf(
            @PathVariable Integer id,
            @Valid @RequestBody SaveAsfRequest request
    ) {
        Asf asf = asfService.update(id, request);
        if (asf == null) {
            throw new IllegalArgumentException("ASF not found");
        }
        return responseMapper.toResponse(asf);
    }

    @DeleteMapping("/{id}")
    public void deleteAsf(@PathVariable Integer id) {
        asfService.delete(id);
    }

    @PutMapping("/{asfId}/signers/{signerId}/primary")
    public void setPrimarySigner(
            @PathVariable Integer asfId,
            @PathVariable Integer signerId,
            @RequestBody boolean isPrimary
    ) {
        asfService.setPrimarySigner(asfId, signerId, isPrimary);
    }
}