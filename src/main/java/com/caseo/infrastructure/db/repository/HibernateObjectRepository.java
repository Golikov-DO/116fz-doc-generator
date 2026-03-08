package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateObjectRepository implements ParentRepository<ObjectModel>, ChildRepository<ObjectModel> {
    @Override
    public ObjectModel findOneByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectModel.class, orgId)
        );
    }

    @Override
    public List<ObjectModel> findManyByParentId(int orgId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectModel where organization.id = :orgId order by id",
                                ObjectModel.class)
                        .setParameter("orgId", orgId)
                        .list()
        );
    }

    @Override
    public ObjectModel findOneById(int id) {
        return HibernateUtil.inSession(session ->
                session.get(ObjectModel.class, id)
        );
    }

    @Override
    public List<ObjectModel> findMany() {
        return HibernateUtil.inSession(session ->
                session.createQuery("from ObjectModel order by id", ObjectModel.class)
                        .list()
        );
    }

    @Override
    public void save(ObjectModel objectModel) {
        HibernateUtil.inTransaction(session -> {
            if (objectModel.getId() == null) {  // Новый объект
                session.persist(objectModel);
            } else {  // Существующий объект
                session.merge(objectModel);
            }
        });
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            ObjectModel objectModel = session.get(ObjectModel.class, id);
            if (objectModel != null) {
                session.remove(objectModel);
            }
        });
    }
}
