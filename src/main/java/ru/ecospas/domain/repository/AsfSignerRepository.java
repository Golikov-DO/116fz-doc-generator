package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfSigner;

import java.util.List;

public interface AsfSignerRepository extends JpaRepository<AsfSigner, Integer> {

    List<AsfSigner> findAllByAsfId(Integer asfId);
}