package ru.ecospas.web.dto.response.object;

public record ObjectImageResponse(

        Integer id,
        String groupKey,
        String caption,
        String linkText

) {
}