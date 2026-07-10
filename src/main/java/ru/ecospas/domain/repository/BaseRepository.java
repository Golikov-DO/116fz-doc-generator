package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.ecospas.domain.model.BaseEntity;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity>
        extends JpaRepository<T, Integer> {

    Class<T> getDomainClass();

    List<T> findManyByParentId(int parentId);

    T findOneByParentId(int parentId);
}