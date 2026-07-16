package ru.ecospas.web.dto.response.object;

public record ObjectCompositionKchsResponse(

        Integer id,
        Integer number,
        String position,
        String fullName,
        String workPhone,
        String cellPhone,
        String homeAddress

) {
}