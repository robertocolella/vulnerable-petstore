package io.swagger.injectablepetstore.repository;

import io.swagger.injectablepetstore.entity.Category;
import io.swagger.injectablepetstore.entity.Pet;
import io.swagger.injectablepetstore.entity.Tag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PetRepositoryImpl implements PetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Object addPet(Pet pet) {
        // Insert the new pet (id, category, name and status)
        Query addPetQuery = entityManager.createNativeQuery(
                "INSERT INTO Pet (category_id, name, status) " +
                        "VALUES (" + pet.getCategory().getId() + ", '" + pet.getName() + "', '" + pet.getStatus() + "')");
        addPetQuery.executeUpdate();

        long lastId = ((BigInteger) entityManager.createNativeQuery(
                "SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
        pet.setId(lastId);

        // Add all photoUrls to the new pet
        for (String photoUrl : pet.getPhotoUrls()) {
            Query addPhotoUrlQuery = entityManager.createNativeQuery(
                    "INSERT INTO Pet_photoUrls (Pet_id, photoUrls) " +
                            "VALUES (" + pet.getId() + ", '" + photoUrl + "')");
            addPhotoUrlQuery.executeUpdate();
        }

        // Add all tags to the new pet
        for (Tag tag : pet.getTags()) {
            Query addTagQuery = entityManager.createNativeQuery(
                    "INSERT INTO Pet_tags (Pet_id, tags_id) " +
                            "VALUES (" + pet.getId() + ", " + tag.getId() + ")");
            addTagQuery.executeUpdate();
        }

        return getPetById(String.valueOf(lastId));
    }

    @Override
    public void deleteAll() {
        Query deleteAllPetPhotoUrlsQuery = entityManager.createNativeQuery(
                "DELETE FROM Pet_photoUrls");
        deleteAllPetPhotoUrlsQuery.executeUpdate();

        Query deleteAllPetTagsQuery = entityManager.createNativeQuery(
                "DELETE FROM Pet_tags");
        deleteAllPetTagsQuery.executeUpdate();

        Query deleteAll = entityManager.createNativeQuery(
                "DELETE FROM Pet");
        deleteAll.executeUpdate();

        Query resetTableQuery = entityManager.createNativeQuery("ALTER TABLE Pet AUTO_INCREMENT = 1");
        resetTableQuery.executeUpdate();
    }

    @Override
    public List<Pet> findAll() {
        List<Pet> petList = new ArrayList<>();
        Query findPetByIdQuery = entityManager.createNativeQuery(
                "SELECT DISTINCT p.* " +
                        "FROM Pet p",
                Pet.class);

        List pets = findPetByIdQuery.getResultList();
        for (Object obj : pets) {
            Pet pet = (Pet) obj;
            sortPet(pet);
            petList.add(pet);
        }

        return petList;
    }

    @Override
    public Object getPetById(String petId) {
        List<Pet> petList = new ArrayList<>();
        Query findPetByIdQuery = entityManager.createNativeQuery(
                "SELECT DISTINCT p.* " +
                        "FROM Pet p " +
                        "WHERE p.id = '" + petId + "'",
                Pet.class);

        List pets = findPetByIdQuery.getResultList();
        for (Object obj : pets) {
            Pet pet = (Pet) obj;
            sortPet(pet);
            if (!petList.contains(pet)) {
                petList.add(pet);
            }
        }

        if (petList.size() == 1)
            return petList.get(0);

        return petList;
    }

    private void sortPet(Pet pet) {
        if (pet != null) {
            pet.getPhotoUrls().sort(String.CASE_INSENSITIVE_ORDER);
            pet.getTags().sort(Comparator.comparing(Tag::getId));
        }
    }

    @Override
    public List<Pet> findPetsByStatus(List<String> status) {
        List<Pet> petList = new ArrayList<>();
        for (String s : status) {
            Query findPetsByStatusQuery = entityManager.createNativeQuery(
                    "SELECT DISTINCT p.* " +
                            "FROM Pet p " +
                            "WHERE p.status = '" + s + "'",
                    Pet.class);

            List pets = findPetsByStatusQuery.getResultList();
            for (Object obj : pets) {
                Pet pet = (Pet) obj;
                sortPet(pet);
                if (!petList.contains(pet)) {
                    petList.add(pet);
                }
            }
        }

        return petList;
    }

    @Override
    public List<Pet> findPetsByTag(List<String> tags) {
        List<Pet> petList = new ArrayList<>();
        for (String tag : tags) {
            Query findPetsByTagsQuery = entityManager.createNativeQuery(
                    "SELECT DISTINCT p.* " +
                            "FROM Pet p JOIN Pet_tags pt ON p.id = pt.Pet_id JOIN Tag t ON pt.tags_id = t.id " +
                            "WHERE t.name = '" + tag + "'",
                    Pet.class);

            List pets = findPetsByTagsQuery.getResultList();
            for (Object obj : pets) {
                Pet pet = (Pet) obj;
                sortPet(pet);
                if (!petList.contains(pet)) {
                    petList.add(pet);
                }
            }
        }

        return petList;
    }

    @Override
    public Pet updatePet(Pet pet) {
//        Query updatePetQuery = entityManager.createNativeQuery(
//                "UPDATE Pet SET category_id = '" + pet.getCategory().getId() + "', " +
//                        "name = '" + pet.getName() + "', status = '" + pet.getStatus() + "' " +
//                        "WHERE id = '" + pet.getId() + "'");
//        updatePetQuery.executeUpdate();
//        List<String> newPetPhotoUrls = pet.getPhotoUrls();
//
//        // Get the old pet photo urls and compare them with the newly specified ones; remove the ones that are not in
//        // the new pet photo urls and are in the old pet photo urls
//        Query oldPetPhotoUrlsQuery = entityManager.createNativeQuery(
//                "SELECT u.photoUrls FROM Pet_photoUrls u WHERE u.Pet_id = '" + pet.getId() + "'");
//        List oldPetPhotoUrlsList = oldPetPhotoUrlsQuery.getResultList();
//        for (Object oldPetPhotoUrlObject : oldPetPhotoUrlsList) {
//            String oldPetPhotoUrl = (String) oldPetPhotoUrlObject;
//            newPetPhotoUrls.remove(oldPetPhotoUrl);
//            if (!newPetPhotoUrls.contains(oldPetPhotoUrl)) {
//                Query removePhotoURL = entityManager.createNativeQuery("DELETE FROM Pet_photoUrls " +
//                        "WHERE photoUrls = '" + oldPetPhotoUrl + "'");
//                removePhotoURL.executeUpdate();
//            }
//        }
//
//        // Add the new photo Urls
//        for (String photoUrl : newPetPhotoUrls) {
//            Query addPhotoURL = entityManager.createNativeQuery("INSERT INTO Pet_photoUrls " +
//                    "VALUES (" + pet.getId() + ", '" + photoUrl + "')");
//            addPhotoURL.executeUpdate();
//        }
//
//        List<Tag> newPetTags = pet.getTags();
//        // Get the old pet photo urls and compare them with the newly specified ones; remove the ones that are not in
//        // the new pet photo urls and are in the old pet photo urls
//        Query oldPetTagsQuery = entityManager.createNativeQuery(
//                "SELECT t.* " +
//                        "FROM Pet_tags pt JOIN Tag t ON pt.tags_id = t.id " +
//                        "WHERE pt.Pet_id = '" + pet.getId() + "'", Tag.class);
//        List oldPetTagsList = oldPetTagsQuery.getResultList();
//        for (Object oldPetTagObject : oldPetTagsList) {
//            Tag oldPetTag = (Tag) oldPetTagObject;
//            newPetTags.remove(oldPetTag);
//            if (!newPetTags.contains(oldPetTag)) {
//                Query removeTag = entityManager.createNativeQuery("DELETE FROM Pet_tags " +
//                        "WHERE Pet_tags.tags_id = '" + oldPetTag.getId() + "'");
//                removeTag.executeUpdate();
//            }
//        }
//
//        // Add the new tags
//        for (Tag tag : pet.getTags()) {
//            Query addTagQuery = entityManager.createNativeQuery("INSERT INTO Pet_tags VALUES" +
//                    " (" + pet.getId() + ", " + tag.getId() + ")");
//            addTagQuery.executeUpdate();
//        }

        Pet updatedPet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/petstore?allowMultiQueries=true&" +
                            "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
                            "serverTimezone=UTC",
                    "alogim", "TcfXuK!A3PG(q28f%Q3MnjvJGDk@7oZ5ymNfgU%*2M#XRUYedn");

            try {
                con.setAutoCommit(false);
                Statement updatePetStatement = con.createStatement();
                String updatePetQuery = "UPDATE Pet SET category_id = '" + pet.getCategory().getId() + "', " +
                        "name = '" + pet.getName() + "', status = '" + pet.getStatus() + "' " +
                        "WHERE id = '" + pet.getId() + "'";
                System.out.println("About to execute = " + updatePetQuery);
                int affectedRows = updatePetStatement.executeUpdate(updatePetQuery);
                if (affectedRows == 0) {
                    throw new SQLException("Updating pet failed, no rows affected.");
                }
                updatePetStatement.close();

                // Get the old pet photo urls and compare them with the newly specified ones; remove the ones that are not in
                // the new pet photo urls and are in the old pet photo urls
                List<String> newPetPhotoUrls = new ArrayList<>(pet.getPhotoUrls());
                Statement getOldPhotoUrlsStatement = con.createStatement();
                String getOldPhotoUrlsQuery = "SELECT photoUrls FROM Pet_photoUrls WHERE Pet_id = " + pet.getId();
                ResultSet getOldPhotoUrlsResultSet = getOldPhotoUrlsStatement.executeQuery(getOldPhotoUrlsQuery);
                List<String> oldPhotoUrls = new ArrayList<>();
                while (getOldPhotoUrlsResultSet.next()) {
                    oldPhotoUrls.add(getOldPhotoUrlsResultSet.getString("photoUrls"));
                }
                getOldPhotoUrlsResultSet.close();
                getOldPhotoUrlsStatement.close();

                for (String oldPetPhotoUrl : oldPhotoUrls) {
                    if (!newPetPhotoUrls.contains(oldPetPhotoUrl)) {
                        Statement removeOldPhotoUrlStatement = con.createStatement();
                        String removeOldPhotoUrlQuery = "DELETE FROM Pet_photoUrls " +
                                "WHERE photoUrls = '" + oldPetPhotoUrl + "'";
                        int removedRows = removeOldPhotoUrlStatement.executeUpdate(removeOldPhotoUrlQuery);
                        if (removedRows == 0) throw new SQLException("Removing old pet photo url failed.");
                        removeOldPhotoUrlStatement.close();
                    }
                    newPetPhotoUrls.remove(oldPetPhotoUrl);
                }

                // Add the new photo Urls
                for (String photoUrl : newPetPhotoUrls) {
                    Statement addNewPhotoUrlStatement = con.createStatement();
                    String addNewPhotoUrlQuery = "INSERT INTO Pet_photoUrls " +
                            "VALUES (" + pet.getId() + ", '" + photoUrl + "')";
                    addNewPhotoUrlStatement.executeUpdate(addNewPhotoUrlQuery);
                    addNewPhotoUrlStatement.close();
                }

                // Get the old pet photo urls and compare them with the newly specified ones; remove the ones that are not in
                // the new pet photo urls and are in the old pet photo urls
                List<Tag> newTags = new ArrayList<>(pet.getTags());
                Statement getOldTagsStatement = con.createStatement();
                String getOldTagsQuery = "SELECT t.* " +
                        "FROM Pet_tags pt JOIN Tag t ON pt.tags_id = t.id " +
                        "WHERE pt.Pet_id = '" + pet.getId() + "'";
                ResultSet getOldTagsResultSet = getOldTagsStatement.executeQuery(getOldTagsQuery);
                List<Tag> oldTags = new ArrayList<>();
                while (getOldTagsResultSet.next()) {
                    oldTags.add(new Tag(getOldTagsResultSet.getLong("id"), getOldTagsResultSet.getString("name")));
                }
                getOldTagsResultSet.close();
                getOldTagsStatement.close();

                for (Tag oldTag : oldTags) {
                    if (!newTags.contains(oldTag)) {
                        Statement removeOldTagStatement = con.createStatement();
                        String removeOldTagQuery = "DELETE FROM Pet_tags " +
                                "WHERE Pet_tags.tags_id = " + oldTag.getId();
                        removeOldTagStatement.executeUpdate(removeOldTagQuery);
                        removeOldTagStatement.close();
                    }
                    newTags.remove(oldTag);
                }

                // Add the new tags
                for (Tag tag : newTags) {
                    Statement addNewTagStatement = con.createStatement();
                    String addNewTagQuery = "INSERT INTO Pet_tags " +
                            "VALUES (" + pet.getId() + ", " + tag.getId() + ")";
                    addNewTagStatement.executeUpdate(addNewTagQuery);
                    addNewTagStatement.close();
                }

                con.commit();

                updatedPet = new Pet();
                updatedPet.setId(pet.getId());

                Statement petStatement = con.createStatement();
                String getUpdatedBasePet = "SELECT p.name, p.status, p.category_id, c.name AS categoryName " +
                        "FROM Pet p JOIN Category c ON p.category_id = c.id WHERE p.id = " + pet.getId();
                ResultSet petResultSet = petStatement.executeQuery(getUpdatedBasePet);
                while (petResultSet.next()) {
                    updatedPet.setName(petResultSet.getString("name"));
                    updatedPet.setStatus(petResultSet.getString("status"));
                    updatedPet.setCategory(new Category(petResultSet.getLong("category_id"), petResultSet.getString(
                            "categoryName")));
                }
                petResultSet.close();
                petStatement.close();

                Statement photoUrlsStatement = con.createStatement();
                String getPhotoUrls = "SELECT photoUrls FROM Pet_photoUrls WHERE Pet_id = " + pet.getId() + " ORDER " +
                        "BY Pet_id";
                ResultSet photoUrlsResultSet = photoUrlsStatement.executeQuery(getPhotoUrls);
                List<String> photoUrls = new ArrayList<>();
                while (photoUrlsResultSet.next()) {
                    photoUrls.add(photoUrlsResultSet.getString("photoUrls"));
                }
                photoUrlsResultSet.close();
                photoUrlsStatement.close();
                updatedPet.setPhotoUrls(photoUrls);

                Statement tagsStatement = con.createStatement();
                String getTags = "SELECT t.id, t.name FROM Tag t JOIN Pet_tags pt ON t.id = pt.tags_id WHERE pt" +
                        ".Pet_id = " + pet.getId() + " ORDER BY t.id";
                ResultSet tagsResultSet = tagsStatement.executeQuery(getTags);
                List<Tag> tags = new ArrayList<>();
                while (tagsResultSet.next()) {
                    tags.add(new Tag(tagsResultSet.getLong("id"), tagsResultSet.getString("name")));
                }
                tagsResultSet.close();
                tagsStatement.close();
                updatedPet.setTags(tags);

            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            } finally {
                con.setAutoCommit(true);
                con.close();
            }

            sortPet(updatedPet);
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        return updatedPet;
    }
}