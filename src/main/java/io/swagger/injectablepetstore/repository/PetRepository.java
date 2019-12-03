package io.swagger.injectablepetstore.repository;

import io.swagger.injectablepetstore.entity.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends PetRepositoryCustom, CrudRepository<Pet, Long> { }
