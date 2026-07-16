package ru.ecospas.web.dto.response.asf;

public record AsfSpecialistsResponse(

        int totalCount,

        int asrTp,
        int asrLrnTer,
        int gzsr,
        int psr,
        int driver,
        int asrLrnSea
) {
}