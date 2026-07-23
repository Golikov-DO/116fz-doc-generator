package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ecospas.domain.model.AsfSigner;

import java.util.List;

public interface AsfSignerRepository extends JpaRepository<AsfSigner, Integer> {

    List<AsfSigner> findAllByAsfId(Integer asfId);

    @Modifying
    @Query("UPDATE AsfSigner s SET s.isPrimary = false WHERE s.asf.id = :asfId")
    void clearPrimaryByAsfId(@Param("asfId") Integer asfId);
}