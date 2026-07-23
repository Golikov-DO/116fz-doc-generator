package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.repository.AsfDocumentImageRepository;
import ru.ecospas.domain.service.AsfDocumentImageService;
import ru.ecospas.web.dto.response.image.ImageUploadResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/asf-images")
@RequiredArgsConstructor
public class AsfImageController {

    private final AsfDocumentImageService imageService;
    private final AsfDocumentImageRepository imageRepository;

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

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable Integer id) {
        return imageRepository.findById(id)
                .map(AsfDocumentImage::getImageBlob)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        imageService.delete(id);
    }
}