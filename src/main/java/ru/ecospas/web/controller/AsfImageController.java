package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.AsfRepository;
import ru.ecospas.domain.service.AsfDocumentImageService;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.dto.response.image.ImageUploadResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/asf-images")
@RequiredArgsConstructor
public class AsfImageController {

    private final AsfDocumentImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageUploadResponse upload(
            @RequestParam Integer asfId,
            @RequestParam String group,
            @RequestParam(defaultValue = "0") Integer imageId,
            @RequestParam MultipartFile file
    ) throws IOException {
        Integer id = imageService.upload(asfId, group, imageId, file.getBytes());
        return new ImageUploadResponse(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        imageService.delete(id);
    }
}