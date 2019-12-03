package io.swagger.injectablepetstore.repository;

import io.swagger.injectablepetstore.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CategoryRepositoryCustom, CrudRepository<Category, Long> {
    Optional<Category> findById(Long aLong);
    List<Category> findCategoriesByName(String name);
}
