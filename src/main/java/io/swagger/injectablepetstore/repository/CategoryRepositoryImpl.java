package io.swagger.injectablepetstore.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void reset() {
        Query deleteAllQuery = entityManager.createNativeQuery("DELETE FROM Category");
        deleteAllQuery.executeUpdate();

        Query resetTableQuery = entityManager.createNativeQuery("ALTER TABLE Category AUTO_INCREMENT = 1");
        resetTableQuery.executeUpdate();
    }
}
