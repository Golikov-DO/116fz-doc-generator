package ru.ecospas.web.dto.request.asf;

public record AsfSignerRequest(

        Integer id,
        String name,
        String position,
        boolean isPrimary
) {
}