package io.swagger.injectablepetstore.repository;

import io.swagger.injectablepetstore.entity.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends TagRepositoryCustom, CrudRepository<Tag, Long> {
    List<Tag> findTagsByName(String name);
}
