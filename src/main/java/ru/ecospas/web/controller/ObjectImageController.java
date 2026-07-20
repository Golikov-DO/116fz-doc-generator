package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.ObjectImageService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.dto.response.image.ImageUploadResponse;

@RestController
@RequestMapping("/api/object-images")
@RequiredArgsConstructor
public class ObjectImageController {

    private final ObjectImageService imageService;
    private final ObjectModelRepository objectRepository;
    private final SecurityService securityService;
    private final CurrentUserService currentUserService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageUploadResponse upload(
            @RequestParam Integer objectId,
            @RequestParam String group,
            @RequestParam MultipartFile file
    ) throws Exception {
        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = currentUserService.requireCurrentUser();
        if (!securityService.hasAccess(user, object.getOrganization())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Integer imageId = imageService.upload(objectId, group, file.getBytes());
        return new ImageUploadResponse(imageId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable Integer id) {
        return imageService.getImage(id);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Integer id) {
        imageService.delete(id);
    }
}