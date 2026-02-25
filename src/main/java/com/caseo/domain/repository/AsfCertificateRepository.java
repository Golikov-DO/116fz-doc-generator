package com.caseo.domain.repository;

import com.caseo.domain.model.AsfCertificate;

import java.sql.SQLException;

public interface AsfCertificateRepository {

    AsfCertificate findByAsfId(int asfId) throws SQLException;

    void save(AsfCertificate certificate, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;

}
