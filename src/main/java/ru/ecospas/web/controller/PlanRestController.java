package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.service.PlanRestService;

import java.io.File;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanRestController {

    private final PlanRestService planRestService;

    @PostMapping("/{objectId}/generate")
    public ResponseEntity<String> generatePlan(@PathVariable Integer objectId) {
        try {
            Path filePath = planRestService.generate(objectId);
            return ResponseEntity.ok("Plan generated: " + filePath.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{objectId}/download")
    public ResponseEntity<Resource> downloadPlan(@PathVariable Integer objectId) {
        try {
            File file = planRestService.getPlanFile(objectId);
            Resource resource = new FileSystemResource(file);

            String fileName = file.getName();
            String encodedName = java.net.URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8)
                    .replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_") +
                                    "\"; filename*=UTF-8''" + encodedName)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}