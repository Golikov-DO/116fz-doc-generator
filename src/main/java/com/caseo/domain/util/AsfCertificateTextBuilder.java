package com.caseo.domain.util;

import com.caseo.domain.model.AsfCertificate;

public class AsfCertificateTextBuilder {

    public static String build(AsfCertificate cert) {

        if (cert == null) return "";

        return "серия № " + cert.certSeries() +
                ", рег. номер " + cert.certNumber() +
                " от " + DateFormatter.dotDate(cert.issueDate()) +
                " г., выданное на основании протокола заседания " +
                cert.issuedBy() + " " + cert.issueBasis() +
                ", сроком действия до " + DateFormatter.russDate(cert.validUntil()) +
                "г.";
    }
}