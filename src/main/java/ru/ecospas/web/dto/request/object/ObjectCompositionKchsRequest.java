package ru.ecospas.web.dto.request.object;

public record ObjectCompositionKchsRequest(

        Integer id,
        Integer number,
        String position,
        String fullName,
        String workPhone,
        String cellPhone,
        String homeAddress

) {
}