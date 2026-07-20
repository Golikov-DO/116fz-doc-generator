package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.service.ObjectService;
import ru.ecospas.web.dto.request.object.SaveObjectRequest;
import ru.ecospas.web.dto.response.object.ObjectListResponse;
import ru.ecospas.web.dto.response.object.ObjectResponse;
import ru.ecospas.web.mapper.object.ObjectResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/organizations/{organizationId}/objects")
@RequiredArgsConstructor
public class ObjectRestController {

    private final ObjectService objectService;
    private final ObjectResponseMapper responseMapper;

    @GetMapping
    public List<ObjectListResponse> getObjects(
            @PathVariable Integer organizationId
    ) {
        return responseMapper.toListResponses(objectService.findAll(organizationId)
        );
    }

    @GetMapping("/{id}")
    public ObjectResponse getObject(
            @PathVariable Integer organizationId,
            @PathVariable Integer id
    ) {

        ObjectModel object = objectService.load(organizationId, id);
        if (object == null) {
            throw new IllegalArgumentException("Object not found");
        }
        return responseMapper.toResponse(object);
    }

    @PostMapping
    public ObjectResponse createObject(
            @PathVariable Integer organizationId,
            @Valid
            @RequestBody
            SaveObjectRequest request
    ) {
        ObjectModel object = objectService.create(organizationId, request);
        return responseMapper.toResponse(object);
    }

    @PutMapping("/{id}")
    public ObjectResponse updateObject(
            @PathVariable Integer organizationId,
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveObjectRequest request
    ) {
        ObjectModel object = objectService.update(organizationId, id, request);
        if (object == null) {
            throw new IllegalArgumentException("Object not found");
        }
        return responseMapper.toResponse(object);
    }

    @DeleteMapping("/{id}")
    public void deleteObject(@PathVariable Integer organizationId, @PathVariable Integer id) {
        objectService.delete(organizationId, id);
    }
}