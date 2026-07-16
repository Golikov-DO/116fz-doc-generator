package ru.ecospas.web.dto.response.object;

public record ObjectPersonsResponsibleResponse(

        Integer id,
        Integer number,
        String fullName,
        String position

) {
}