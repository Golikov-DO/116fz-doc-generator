package ru.ecospas.web.dto.request.asf;

import java.time.LocalDate;

public record AsfCertificateRequest(

        String certNumber,
        String certSeries,
        String issuedBy,
        String issueBasis,

        LocalDate issueDate,
        LocalDate validUntil
) {
}