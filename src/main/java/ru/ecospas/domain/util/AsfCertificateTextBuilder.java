package ru.ecospas.domain.util;

import ru.ecospas.domain.model.AsfCertificate;

public class AsfCertificateTextBuilder {

    private AsfCertificateTextBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String build(AsfCertificate cert) {

        if (cert == null) return "";

        return "серия № " + cert.getCertNumber() +
                ", рег. номер " + cert.getCertSeries() +
                " от " + DocumentOutputFormatter.dotDate(String.valueOf(cert.getIssueDate())) +
                " г., выданное на основании протокола заседания " +
                cert.getIssuedBy() + " " + cert.getIssueBasis() +
                ", сроком действия до " + DocumentOutputFormatter.russDate(String.valueOf(cert.getValidUntil())) +
                "г";
    }
}