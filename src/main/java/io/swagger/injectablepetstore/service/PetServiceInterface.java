package io.swagger.injectablepetstore.service;

import io.swagger.injectablepetstore.entity.Pet;
import io.swagger.injectablepetstore.exceptions.PetNotFoundException;

import java.util.List;

public interface PetServiceInterface {
    Object addPet(Pet pet);
    void deleteAll();
    void deletePet(String petId) throws PetNotFoundException;
    List<Pet> findPetsByStatus(List<String> status);
    List<Pet> findPetsByTags(List<String> tags);
    Object getPetById(String petId) throws PetNotFoundException;
    List<Pet> getPets();
    Pet updatePet(Pet pet) throws PetNotFoundException;
}
