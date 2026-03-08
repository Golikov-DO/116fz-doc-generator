package com.caseo.domain.util;

import com.caseo.domain.model.AsfCertificate;

public class AsfCertificateTextBuilder {

    public static String build(AsfCertificate cert) {

        if (cert == null) return "";

        return "серия № " + cert.getCertSeries() +
                ", рег. номер " + cert.getCertNumber() +
                " от " + DocumentOutputFormatter.dotDate(cert.getIssueDate()) +
                " г., выданное на основании протокола заседания " +
                cert.getIssuedBy() + " " + cert.getIssueBasis() +
                ", сроком действия до " + DocumentOutputFormatter.russDate(cert.getValidUntil()) +
                "г";
    }
}