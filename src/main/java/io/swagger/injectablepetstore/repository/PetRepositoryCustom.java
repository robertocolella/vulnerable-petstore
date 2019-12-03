package io.swagger.injectablepetstore.repository;

import io.swagger.injectablepetstore.entity.Pet;

import java.util.List;

public interface PetRepositoryCustom {
    Object addPet(Pet pet);
    void deleteAll();
    List<Pet> findAll();
    Object getPetById(String id);
    List<Pet> findPetsByStatus(List<String> status);
    List<Pet> findPetsByTag(List<String> tags);
    Pet updatePet(Pet pet);
}
