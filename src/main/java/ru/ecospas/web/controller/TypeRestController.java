package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ReferenceType;
import ru.ecospas.domain.service.ReferenceTypeService;
import ru.ecospas.web.dto.request.type.SaveTypeRequest;
import ru.ecospas.web.dto.response.type.TypeListResponse;
import ru.ecospas.web.dto.response.type.TypeResponse;
import ru.ecospas.web.mapper.type.TypeResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypeRestController {

    private final ReferenceTypeService typeService;
    private final TypeResponseMapper responseMapper;

    @GetMapping
    public List<TypeListResponse> getTypes() {
        return responseMapper.toListResponses(typeService.findAll());
    }

    @GetMapping("/{id}")
    public TypeResponse getType(@PathVariable Integer id) {
        ReferenceType type = typeService.loadRest(id);
        if (type == null) {
            throw new IllegalArgumentException("Type not found");
        }
        return responseMapper.toResponse(type);
    }

    @PostMapping
    public TypeResponse createType(
            @Valid
            @RequestBody
            SaveTypeRequest request
    ) {
        return responseMapper.toResponse(typeService.create(request));
    }

    @PutMapping("/{id}")
    public TypeResponse updateType(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveTypeRequest request
    ) {
        ReferenceType type = typeService.update(id, request);
        if (type == null) {
            throw new IllegalArgumentException("Type not found");
        }
        return responseMapper.toResponse(type);
    }

    @DeleteMapping("/{id}")
    public void deleteType(@PathVariable Integer id) {
        typeService.deleteRest(id);
    }
}