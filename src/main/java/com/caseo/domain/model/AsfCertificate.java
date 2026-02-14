package com.caseo.domain.model;

public record AsfCertificate(
        String certNumber,
        String certSeries,
        String issuedBy,
        String issueBasis,
        String issueDate,
        String validUntil
) {
}
