package io.swagger.injectablepetstore.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class TagRepositoryImpl implements TagRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void reset() {
        Query deleteAllQuery = entityManager.createNativeQuery("DELETE FROM Tag");
        deleteAllQuery.executeUpdate();

        Query resetTable = entityManager.createNativeQuery("ALTER TABLE Tag AUTO_INCREMENT = 1");
        resetTable.executeUpdate();
    }
}
