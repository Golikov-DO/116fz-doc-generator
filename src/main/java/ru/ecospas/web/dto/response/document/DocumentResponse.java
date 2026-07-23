package ru.ecospas.web.dto.response.document;

public record DocumentResponse(
        String organizationName,
        String fileName,
        long size
) {
}