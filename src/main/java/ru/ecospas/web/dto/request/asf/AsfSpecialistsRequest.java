package ru.ecospas.web.dto.request.asf;

public record AsfSpecialistsRequest(

        int totalCount,

        int asrTp,
        int asrLrnTer,
        int gzsr,
        int psr,
        int driver,
        int asrLrnSea
) {
}