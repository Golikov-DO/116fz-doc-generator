package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfDocumentImage;

import java.util.List;

public interface AsfDocumentImageRepository extends JpaRepository<AsfDocumentImage, Integer> {

    List<AsfDocumentImage> findAllByAsfId(Integer asfId);
}