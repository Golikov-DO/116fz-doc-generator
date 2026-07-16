package ru.ecospas.web.dto.request.object;

public record ObjectImageRequest(

        Integer id,
        String groupKey,
        String caption,
        String linkText

) {
}