package io.swagger.injectablepetstore.controller;

import io.swagger.injectablepetstore.entity.Pet;
import io.swagger.injectablepetstore.exceptions.PetNotFoundException;
import io.swagger.injectablepetstore.service.CategoryServiceInterface;
import io.swagger.injectablepetstore.service.PetServiceInterface;
import io.swagger.injectablepetstore.service.TagServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class PetApiController implements PetApi {
    private static final Logger log = LoggerFactory.getLogger(PetApiController.class);

    private final CategoryServiceInterface categoryService;
    private final PetServiceInterface petService;
    private final TagServiceInterface tagService;
    private final HttpServletRequest request;

    @Autowired
    public PetApiController(CategoryServiceInterface categoryService, PetServiceInterface petService,
                            TagServiceInterface tagService, HttpServletRequest request) {
        this.categoryService = categoryService;
        this.petService = petService;
        this.tagService = tagService;
        this.request = request;
    }

    @Override
    public ResponseEntity<Pet> addPet(@Valid Pet body) {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Pet pet = (Pet) petService.addPet(body);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deletePet(String petId, String apiKey) {
        try {
            petService.deletePet(petId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PetNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<Pet>> findPetsByStatus(List<String> status) {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Pet> returnList = petService.findPetsByStatus(status);
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    public ResponseEntity<List<Pet>> findPetsByTags(List<String> tags) {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Pet> returnList = petService.findPetsByTags(tags);
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getPetById(String petId) {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            Object object = petService.getPetById(petId);
            return new ResponseEntity<>(object, HttpStatus.OK);
        } catch (PetNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<Pet>> getPets() {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Pet> pets = petService.getPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Pet> updatePet(@Valid Pet body) {
        String accept = request.getHeader("Accept");
        if (accept == null || !accept.contains("application/json"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            Pet updatedPet = petService.updatePet(body);
            return new ResponseEntity<>(updatedPet, HttpStatus.OK);
        } catch (PetNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity resetPetStore() {
        petService.deleteAll();
        categoryService.deleteAll();
        tagService.deleteAll();

        return new ResponseEntity(HttpStatus.OK);
    }
}
