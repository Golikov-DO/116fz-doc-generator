package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.service.DocumentService;
import ru.ecospas.web.dto.response.document.DocumentResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentRestController {

    private final DocumentService documentService;

    @GetMapping
    public List<DocumentResponse> getDocuments() throws IOException {
        return documentService.findAll();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam String organization,
            @RequestParam String file
    ) {

        Path path = documentService.getDocument(organization, file);

        Resource resource = new FileSystemResource(path);

        String filename = URLEncoder.encode(
                path.getFileName().toString(),
                StandardCharsets.UTF_8
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + filename
                )
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam String organization,
            @RequestParam String file
    ) throws IOException {

        documentService.delete(organization, file);

        return ResponseEntity.noContent().build();
    }
}