package ru.ecospas.web.dto.response.object;

public record ObjectStructureResponse(

        Integer id,
        Integer num,
        String name,
        String likelyIds,
        String dangerousIds

) {
}