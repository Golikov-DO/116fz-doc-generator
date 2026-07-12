package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceTableTitle;

public interface ReferenceTableTitleRepository
        extends JpaRepository<ReferenceTableTitle, Integer> {
}