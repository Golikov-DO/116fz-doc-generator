package ru.ecospas.domain.util;

import ru.ecospas.domain.model.AsfCertificate;

public class AsfCertificateTextBuilder {

    private AsfCertificateTextBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String build(AsfCertificate cert) {

        if (cert == null) return "";

        String issueDateStr = cert.getIssueDate() != null
                ? DocumentOutputFormatter.dotDate(String.valueOf(cert.getIssueDate()))
                : "";
        String validUntilStr = cert.getValidUntil() != null
                ? DocumentOutputFormatter.russDate(String.valueOf(cert.getValidUntil()))
                : "";

        return "серия № " + cert.getCertNumber() +
                ", рег. номер " + cert.getCertSeries() +
                (issueDateStr.isEmpty() ? "" : " от " + issueDateStr + " г.") +
                ", выданное на основании протокола заседания " +
                cert.getIssuedBy() + " " + cert.getIssueBasis() +
                (validUntilStr.isEmpty() ? "" : ", сроком действия до " + validUntilStr + "г");
    }
}