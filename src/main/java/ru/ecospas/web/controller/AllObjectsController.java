package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ecospas.domain.service.ObjectService;
import ru.ecospas.web.dto.response.object.ObjectWithOrgResponse;

import java.util.List;

@RestController
@RequestMapping("/api/objects")
@RequiredArgsConstructor
public class AllObjectsController {
    
    private final ObjectService objectService;
    
    @GetMapping
    public List<ObjectWithOrgResponse> getAllObjects() {
        return objectService.findAllObjectsWithOrg();
    }
}