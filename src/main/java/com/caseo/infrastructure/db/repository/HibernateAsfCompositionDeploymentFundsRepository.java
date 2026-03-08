package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;
import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.infrastructure.db.HibernateUtil;

import java.util.List;

public class HibernateAsfCompositionDeploymentFundsRepository implements ChildRepository<AsfCompositionDeploymentFunds> {
    @Override
    public AsfCompositionDeploymentFunds findOneByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.get(AsfCompositionDeploymentFunds.class, asfId)
        );
    }

    @Override
    public List<AsfCompositionDeploymentFunds> findManyByParentId(int asfId) {
        return HibernateUtil.inSession(session ->
                session.createQuery("from AsfCompositionDeploymentFunds where asf.id = :asfId order by id",
                                AsfCompositionDeploymentFunds.class)
                        .setParameter("asfId", asfId)
                        .list()
        );
    }

    @Override
    public void save(AsfCompositionDeploymentFunds compositionDeploymentFunds) {
        HibernateUtil.inTransaction(session -> session.merge(compositionDeploymentFunds));
    }

    @Override
    public void deleteById(int id) {
        HibernateUtil.inTransaction(session -> {
            AsfCompositionDeploymentFunds compositionDeploymentFunds = session.get(AsfCompositionDeploymentFunds.class, id);
            if (compositionDeploymentFunds != null) {
                session.remove(compositionDeploymentFunds);
            }
        });
    }
}
