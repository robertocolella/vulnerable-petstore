package io.swagger.injectablepetstore.service;

import io.swagger.injectablepetstore.entity.Category;
import io.swagger.injectablepetstore.entity.Pet;
import io.swagger.injectablepetstore.entity.Tag;
import io.swagger.injectablepetstore.exceptions.PetNotFoundException;
import io.swagger.injectablepetstore.repository.CategoryRepository;
import io.swagger.injectablepetstore.repository.PetRepository;
import io.swagger.injectablepetstore.repository.TagRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService implements PetServiceInterface {

    private final CategoryRepository categoryRepository;
    private final PetRepository petRepository;
    private final TagRepository tagRepository;

    public PetService(CategoryRepository categoryRepository, PetRepository petRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.petRepository = petRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Object addPet(Pet pet) {
        // Check if the category exists, otherwise add it to the database
        pet.setCategory(addCategoryIfMissing(pet.getCategory()));

        // Check if the tags exist, otherwise add them to the database
        pet.setTags(addTagsIfMissing(pet.getTags()));

        // Set the id to null so that a new pet is added in case a pet with the same id is already existing
        pet.setId(null);

        // Save the pet
        return petRepository.addPet(pet);
    }

    @Override
    public void deleteAll() {
        petRepository.deleteAll();
    }

    @Override
    public void deletePet(String petId) throws PetNotFoundException {
        Object obj = petRepository.getPetById(petId);
        if (obj instanceof ArrayList) {
            ArrayList petList = (ArrayList) obj;
            if (petList.isEmpty())
                obj = null;

            for (Object object : petList) {
                Pet pet = (Pet) object;
                Hibernate.initialize(pet.getPhotoUrls());
                Hibernate.initialize(pet.getTags());
                petRepository.delete(pet);
            }
        } else if (obj != null) {
            Pet pet = (Pet) obj;
            Hibernate.initialize(pet.getPhotoUrls());
            Hibernate.initialize(pet.getTags());
            petRepository.delete(pet);
        }

        if (obj == null)
            throw new PetNotFoundException("Pet with id \"" + petId + "\" does not exist.");
    }

    @Override
    public List<Pet> findPetsByStatus(List<String> status) {
        return petRepository.findPetsByStatus(status);
    }

    @Override
    public List<Pet> findPetsByTags(List<String> tags) {
        return petRepository.findPetsByTag(tags);
    }

    @Override
    public Object getPetById(String petId) throws PetNotFoundException {
        Object obj = petRepository.getPetById(petId);
        if (obj instanceof ArrayList) {
            ArrayList petList = (ArrayList) obj;
            if (petList.isEmpty())
                obj = null;
        }

        if (obj == null)
            throw new PetNotFoundException("Pet with id " + petId + " does not exist.");

        return obj;
    }

    @Override
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    @Override
    public Pet updatePet(Pet pet) throws PetNotFoundException {
        Object obj = petRepository.getPetById(String.valueOf(pet.getId()));
        Pet toBeUpdated = null;
        if (obj instanceof ArrayList) {
            ArrayList petList = (ArrayList) obj;
            if (!petList.isEmpty())
                toBeUpdated = (Pet) petList.get(0);
        } else if (obj != null) {
            toBeUpdated = (Pet) obj;
        }

        if (toBeUpdated == null)
            throw new PetNotFoundException("Pet with id " + pet.getId() + " does not exist.");

        // Check if the category exists, otherwise add it to the database
        pet.setCategory(addCategoryIfMissing(pet.getCategory()));

        // Check if the tags exist, otherwise add them to the database
        pet.setTags(addTagsIfMissing(pet.getTags()));

        return petRepository.updatePet(pet);
    }

    private List<Tag> addTagsIfMissing(List<Tag> tags) {
        // Check if the tags exist, otherwise add them to the database
        List<Tag> petTags = new ArrayList<>();
        for (Tag tag : tags) {
            Optional<Tag> tagById = tagRepository.findById(tag.getId());
            List<Tag> tagsByName = tagRepository.findTagsByName(tag.getName());
            if (tagById.isPresent() && tagById.get().getName().equals(tag.getName())) {
                petTags.add(tag);
            } else if (!tagById.isPresent() && !tagsByName.isEmpty()) {
                petTags.add(tagsByName.get(0));
            } else {
                tag.setId(null);
                Tag newTag = tagRepository.save(tag);
                petTags.add(newTag);
            }
        }

        return petTags;
    }

    private Category addCategoryIfMissing(Category category) {
        // Check if the category exists, otherwise add it to the database
        Optional<Category> categoryById = categoryRepository.findById(category.getId());
        List<Category> categoryByName = categoryRepository.findCategoriesByName(category.getName());
        Category petCategory;
        if (categoryById.isPresent() && categoryById.get().getName().equals(category.getName())) {
            petCategory = categoryById.get();
        } else if (!categoryById.isPresent() && !categoryByName.isEmpty()) {
            petCategory = categoryByName.get(0);
        } else {
            category.setId(null);
            petCategory = categoryRepository.save(category);
        }

        return petCategory;
    }
}
