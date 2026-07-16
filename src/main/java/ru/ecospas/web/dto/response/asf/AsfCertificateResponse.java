package ru.ecospas.web.dto.response.asf;

import java.time.LocalDate;

public record AsfCertificateResponse(

        String certNumber,
        String certSeries,
        String issuedBy,
        String issueBasis,

        LocalDate issueDate,
        LocalDate validUntil
) {
}