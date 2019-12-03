package io.swagger.injectablepetstore;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.swagger.injectablepetstore.entity.Category;
import io.swagger.injectablepetstore.entity.Pet;
import io.swagger.injectablepetstore.entity.Tag;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetStoreApplicationTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost/v2/pet";
        RestAssured.port = 8081;

        resetDatabase();
    }

    private void resetDatabase() {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/reset");
        TestCase.assertEquals(200, response.statusCode());
    }

    @Test
    public void contextLoads() {}

    @Test
    public void addPetTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));
        Pet pet = new Pet(0L, category, "Pet 0",
                Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available");

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.contentType(ContentType.JSON);
        httpRequest.accept(ContentType.JSON);
        httpRequest.body(new Gson().toJson(pet));

        Response response = httpRequest.post("/");
        TestCase.assertEquals(200, response.statusCode());

        Pet addedPet = response.as(Pet.class);
        TestCase.assertEquals(pet.getCategory().getName(), addedPet.getCategory().getName());
        TestCase.assertEquals(pet.getName(), addedPet.getName());
        TestCase.assertEquals(pet.getPhotoUrls(), addedPet.getPhotoUrls());
        TestCase.assertEquals(pet.getTags().size(), addedPet.getTags().size());
        List<Tag> tags = pet.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            TestCase.assertEquals(tag.getName(), addedPet.getTags().get(i).getName());
        }
        TestCase.assertEquals(pet.getStatus(), addedPet.getStatus());
    }

    @Test
    public void deletePetTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));
        Pet pet = new Pet(0L, category, "Pet 0",
                Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available");

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.contentType(ContentType.JSON);
        httpRequest.accept(ContentType.JSON);
        httpRequest.body(new Gson().toJson(pet));

        Response response = httpRequest.post("/");
        TestCase.assertEquals(200, response.statusCode());
        Pet addedPet = response.getBody().as(Pet.class);

        RequestSpecification deleteHttpRequest = RestAssured.given();
        deleteHttpRequest.accept(ContentType.JSON);
        deleteHttpRequest.pathParam("petId", addedPet.getId());

        Response deletePetResponse = deleteHttpRequest.when().delete("/{petId}");
        TestCase.assertEquals(200, response.statusCode());
        TestCase.assertEquals("application/json;charset=UTF-8", response.getContentType());

        RequestSpecification getPetHttpRequest = RestAssured.given();
        getPetHttpRequest.accept(ContentType.JSON);
        getPetHttpRequest.pathParam("petId", addedPet.getId());

        Response getPetResponse = getPetHttpRequest.get("/{petId}");
        TestCase.assertEquals(404, getPetResponse.statusCode());
    }

    @Test
    public void findPetsByStatusTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));

        List<Pet> petsToAdd = Arrays.asList(
                new Pet(0L, category, "Pet 1",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 2",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "sold"),
                new Pet(0L, category, "Pet 3",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 4",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "pending"),
                new Pet(0L, category, "Pet 5",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"));

        List<Pet> addedPets = new ArrayList<>();
        for (Pet pet : petsToAdd) {
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.contentType(ContentType.JSON);
            httpRequest.accept(ContentType.JSON);
            httpRequest.body(new Gson().toJson(pet));

            Response response = httpRequest.post("/");
            TestCase.assertEquals(200, response.statusCode());
            addedPets.add(response.getBody().as(Pet.class));
        }

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.accept(ContentType.JSON);
        httpRequest.queryParam("status", "available");

        Response response = httpRequest.get("/findByStatus");
        TestCase.assertEquals(200, response.statusCode());
        List<Pet> pets = Arrays.asList(response.getBody().as(Pet[].class));
        TestCase.assertEquals(3, pets.size());
        List<Pet> expectedPets = Arrays.asList(addedPets.get(0), addedPets.get(2), addedPets.get(4));
        TestCase.assertTrue(expectedPets.containsAll(pets));
        TestCase.assertTrue(pets.containsAll(expectedPets));
    }

    @Test
    public void findPetsBySingleTagTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));

        List<Pet> petsToAdd = Arrays.asList(
                new Pet(0L, category, "Pet 1", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Collections.singletonList(tagList.get(0)), "available"),
                new Pet(0L, category, "Pet 2", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(0), tagList.get(2)), "sold"),
                new Pet(0L, category, "Pet 3", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(0), tagList.get(2)), "available"),
                new Pet(0L, category, "Pet 4", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(1), tagList.get(2)), "pending"),
                new Pet(0L, category, "Pet 5", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Collections.singletonList(tagList.get(2)), "available"));

        List<Pet> addedPets = new ArrayList<>();
        for (Pet pet : petsToAdd) {
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.contentType(ContentType.JSON);
            httpRequest.accept(ContentType.JSON);
            httpRequest.body(new Gson().toJson(pet));

            Response response = httpRequest.post("/");
            TestCase.assertEquals(200, response.statusCode());
            addedPets.add(response.getBody().as(Pet.class));
        }

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.accept(ContentType.JSON);
        httpRequest.queryParam("tags", tagList.get(1).getName());

        Response response = httpRequest.get("/findByTags");
        TestCase.assertEquals(200, response.statusCode());
        List<Pet> pets = Arrays.asList(response.getBody().as(Pet[].class));
        TestCase.assertEquals(1, pets.size());
        TestCase.assertEquals(addedPets.get(3), pets.get(0));
    }

    @Test
    public void findPetsByMultipleTagTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));

        List<Pet> petsToAdd = Arrays.asList(
                new Pet(0L, category, "Pet 1", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Collections.singletonList(tagList.get(2)), "available"),
                new Pet(0L, category, "Pet 2", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(0), tagList.get(2)), "sold"),
                new Pet(0L, category, "Pet 3", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(0), tagList.get(2)), "available"),
                new Pet(0L, category, "Pet 4", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Arrays.asList(tagList.get(0), tagList.get(2)), "pending"),
                new Pet(0L, category, "Pet 5", Arrays.asList("http://www.google.com/", "https://imgur.com/"),
                        Collections.singletonList(tagList.get(1)), "available"));

        List<Pet> addedPets = new ArrayList<>();
        for (Pet pet : petsToAdd) {
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.contentType(ContentType.JSON);
            httpRequest.accept(ContentType.JSON);
            httpRequest.body(new Gson().toJson(pet));

            Response response = httpRequest.post("/");
            TestCase.assertEquals(200, response.statusCode());
            addedPets.add(response.getBody().as(Pet.class));
        }

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.accept(ContentType.JSON);
        httpRequest.queryParam("tags", tagList.get(0).getName(), tagList.get(2).getName());

        Response response = httpRequest.get("/findByTags");
        TestCase.assertEquals(200, response.statusCode());
        List<Pet> pets = Arrays.asList(response.getBody().as(Pet[].class));
        TestCase.assertEquals(4, pets.size());
        List<Pet> expectedPets = new ArrayList<>(addedPets.subList(0, 4));
        TestCase.assertTrue(expectedPets.containsAll(pets));
        TestCase.assertTrue(pets.containsAll(expectedPets));
    }

    @Test
    public void getPetByIdTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));
        Pet pet = new Pet(0L, category, "Pet 0",
                Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available");

        RequestSpecification addPetHttpRequest = RestAssured.given();
        addPetHttpRequest.contentType(ContentType.JSON);
        addPetHttpRequest.accept(ContentType.JSON);
        addPetHttpRequest.body(new Gson().toJson(pet));

        Response addPetResponse = addPetHttpRequest.post("/");
        TestCase.assertEquals(200, addPetResponse.statusCode());
        Pet addedPet = addPetResponse.as(Pet.class);
        TestCase.assertEquals(pet.getCategory().getName(), addedPet.getCategory().getName());
        TestCase.assertEquals(pet.getName(), addedPet.getName());
        TestCase.assertEquals(pet.getPhotoUrls(), addedPet.getPhotoUrls());
        TestCase.assertEquals(pet.getTags().size(), addedPet.getTags().size());
        List<Tag> tags = pet.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            TestCase.assertEquals(tag.getName(), addedPet.getTags().get(i).getName());
        }
        TestCase.assertEquals(pet.getStatus(), addedPet.getStatus());

        RequestSpecification getPetHttpRequest = RestAssured.given();
        getPetHttpRequest.accept(ContentType.JSON);
        getPetHttpRequest.pathParam("petId", addedPet.getId());

        Response getPetResponse = getPetHttpRequest.get("/{petId}");
        TestCase.assertEquals(200, getPetResponse.statusCode());
        TestCase.assertEquals("application/json;charset=UTF-8", getPetResponse.contentType());
        Pet gottenPet = getPetResponse.getBody().as(Pet.class);
        TestCase.assertEquals(pet.getCategory().getName(), gottenPet.getCategory().getName());
        TestCase.assertEquals(pet.getName(), gottenPet.getName());
        TestCase.assertEquals(pet.getPhotoUrls(), gottenPet.getPhotoUrls());
        TestCase.assertEquals(pet.getTags().size(), gottenPet.getTags().size());
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            TestCase.assertEquals(tag.getName(), gottenPet.getTags().get(i).getName());
        }
        TestCase.assertEquals(pet.getStatus(), gottenPet.getStatus());
    }

    @Test
    public void getPetsTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));

        List<Pet> petsToAdd = Arrays.asList(
                new Pet(0L, category, "Pet 1",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 2",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "sold"),
                new Pet(0L, category, "Pet 3",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 4",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "pending"),
                new Pet(0L, category, "Pet 5",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"));

        List<Pet> addedPets = new ArrayList<>();
        for (Pet pet : petsToAdd) {
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.contentType(ContentType.JSON);
            httpRequest.accept(ContentType.JSON);
            httpRequest.body(new Gson().toJson(pet));

            Response response = httpRequest.post("/");
            TestCase.assertEquals(200, response.statusCode());
            addedPets.add(response.getBody().as(Pet.class));
        }

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.accept(ContentType.JSON);

        Response response = httpRequest.get("/");
        TestCase.assertEquals(200, response.statusCode());
        List<Pet> gottenPets = Arrays.asList(response.getBody().as(Pet[].class));
        TestCase.assertEquals(addedPets.size(), gottenPets.size());
        TestCase.assertTrue(addedPets.containsAll(gottenPets));
        TestCase.assertTrue(gottenPets.containsAll(addedPets));
    }

    @Test
    public void updatePetTest() {
        Category category = new Category(0L, "Category 0");
        List<Tag> tagList = Arrays.asList(
                new Tag(0L, "Tag 0"),
                new Tag(1L, "Tag 1"),
                new Tag(2L, "Tag 2"));

        List<Pet> petsToAdd = Arrays.asList(
                new Pet(0L, category, "Pet 1",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 2",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "sold"),
                new Pet(0L, category, "Pet 3",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"),
                new Pet(0L, category, "Pet 4",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "pending"),
                new Pet(0L, category, "Pet 5",
                        Arrays.asList("http://www.google.com/", "https://imgur.com/"), tagList, "available"));

        List<Pet> addedPets = new ArrayList<>();
        for (Pet pet : petsToAdd) {
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.contentType(ContentType.JSON);
            httpRequest.accept(ContentType.JSON);
            httpRequest.body(new Gson().toJson(pet));

            Response response = httpRequest.post("/");
            TestCase.assertEquals(200, response.statusCode());
            addedPets.add(response.getBody().as(Pet.class));
        }

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.accept(ContentType.JSON);
        httpRequest.contentType(ContentType.JSON);
        Pet petToUpdate = addedPets.get(2);
        petToUpdate.setName("Modified Pet 3");
        httpRequest.body(new Gson().toJson(petToUpdate));

        Response response = httpRequest.put("/");
        TestCase.assertEquals(200, response.statusCode());
        Pet gottenPet = response.getBody().as(Pet.class);
        TestCase.assertNotNull(gottenPet);
        TestCase.assertEquals(petToUpdate.getCategory().getName(), gottenPet.getCategory().getName());
        TestCase.assertEquals(petToUpdate.getName(), gottenPet.getName());
        TestCase.assertTrue(petToUpdate.getPhotoUrls().containsAll(gottenPet.getPhotoUrls()));
        TestCase.assertEquals(petToUpdate.getTags().size(), gottenPet.getTags().size());
        TestCase.assertTrue(gottenPet.getTags().containsAll(petToUpdate.getTags()));
        TestCase.assertEquals(petToUpdate.getStatus(), gottenPet.getStatus());
}

    @After
    public void clean() {
        resetDatabase();
    }
}
