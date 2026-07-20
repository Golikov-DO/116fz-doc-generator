package ru.ecospas.web.dto.request.object;

public record ObjectStructureRequest(

        Integer id,
        Integer num,
        String name,
        String likelyIds,
        String dangerousIds

) {
}