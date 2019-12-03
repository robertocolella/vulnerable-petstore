package io.swagger.injectablepetstore.controller;

import io.swagger.injectablepetstore.entity.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/v2/pet")
public interface PetApi {

    @PostMapping(value = "", produces = { "application/json" }, consumes = { "application/json" })
    ResponseEntity<Pet> addPet(@Valid @RequestBody Pet body);

    @DeleteMapping(value = "/{petId}", produces = { "application/xml", "application/json" })
    ResponseEntity deletePet(@PathVariable(value = "petId") String petId,
                             @RequestHeader(value = "api_key", required = false) String apiKey);

    @GetMapping(value = "/findByStatus", produces = { "application/json" })
    ResponseEntity<List<Pet>> findPetsByStatus(@RequestParam(value = "status") List<String> status);

    @GetMapping(value = "/findByTags", produces = { "application/json" })
    ResponseEntity<List<Pet>> findPetsByTags(@RequestParam(value = "tags") List<String> tags);

    @GetMapping(value = "/{petId}", produces = { "application/json" })
    ResponseEntity<Object> getPetById(@PathVariable(name = "petId") String petId);

    @GetMapping(value = "", produces = { "application/json" })
    ResponseEntity<List<Pet>> getPets();

    @PutMapping(value = "", produces = { "application/json" }, consumes = { "application/json" })
    ResponseEntity<Pet> updatePet(@Valid @RequestBody Pet body);

    @GetMapping(value = "/reset")
    ResponseEntity resetPetStore();
}
