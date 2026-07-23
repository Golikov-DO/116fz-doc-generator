package ru.ecospas.web.dto.response.asf;

public record AsfSignerResponse(

        Integer id,
        String name,
        String position,
        Boolean isPrimary
) {
}