package ru.ecospas.web.dto.request.asf;

public record AsfImageRequest(

        Integer id,

        String groupKey,
        String nameDocument
) {
}