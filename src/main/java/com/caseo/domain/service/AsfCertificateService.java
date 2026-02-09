package com.caseo.domain.service;

import com.caseo.domain.model.AsfCertificate;
import com.caseo.domain.repository.AsfCertificateRepository;

import java.sql.SQLException;

public class AsfCertificateService {

    AsfCertificateRepository asfCertificateRepository;

    public AsfCertificateService(AsfCertificateRepository asfCertificateRepository) {
        this.asfCertificateRepository = asfCertificateRepository;
    }

    public AsfCertificate getByAsfId(int asfId) throws SQLException {
        return asfCertificateRepository.findByAsfId(asfId);
    }

}
