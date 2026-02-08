package com.caseo.domain.model;

public record AsfCertificate(int id, String certNumber, String certSeries, String issuedBy, String issueBasis,
                             String issueDate, String validUntil) {

}
