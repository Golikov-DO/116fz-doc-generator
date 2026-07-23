package ru.ecospas.web.dto.request.object;

public record ObjectPersonsResponsibleRequest(

        Integer id,
        Integer number,
        String fullName,
        String position

) {
}