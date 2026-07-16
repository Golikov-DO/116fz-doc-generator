package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.service.RegionalAuthoritiesService;
import ru.ecospas.web.dto.request.city.SaveCityRequest;
import ru.ecospas.web.dto.response.city.CityListResponse;
import ru.ecospas.web.dto.response.city.CityResponse;
import ru.ecospas.web.mapper.city.CityResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityRestController {

    private final RegionalAuthoritiesService cityService;
    private final CityResponseMapper responseMapper;

    @GetMapping
    public List<CityListResponse> getCities() {
        return responseMapper.toListResponses(cityService.findAll());
    }

    @GetMapping("/{id}")
    public CityResponse getCity(@PathVariable Integer id) {

        ReferenceCity city = cityService.loadRest(id);
        if (city == null) {
            throw new IllegalArgumentException("City not found");
        }
        return responseMapper.toResponse(city);
    }

    @PostMapping
    public CityResponse createCity(
            @Valid
            @RequestBody
            SaveCityRequest request
    ) {

        ReferenceCity city = cityService.create(request);
        return responseMapper.toResponse(city);
    }

    @PutMapping("/{id}")
    public CityResponse updateCity(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveCityRequest request
    ) {

        ReferenceCity city = cityService.update(id, request);
        if (city == null) {
            throw new IllegalArgumentException("City not found");
        }
        return responseMapper.toResponse(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Integer id) {
        cityService.deleteRest(id);
    }
}