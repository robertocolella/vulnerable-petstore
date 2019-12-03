package io.swagger.injectablepetstore.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = null;

    @ManyToOne
    @JoinColumn
    private Category category;

    @Column(name = "name")
    private String name = null;

    @ElementCollection
    @Column(name = "photoUrls")
    private List<String> photoUrls = new ArrayList<>();

    @ManyToMany
    @JoinColumn(name = "tag_id")
    private List<Tag> tags = new ArrayList<>();

    @Column(name = "status")
    private String status = "available";

    public Pet() {}

    public Pet(Long id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }

    /**
     * Returns the id of the pet
     * @return a Long containing the id of the pet
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the pet
     * @param id a Long containing the id of the pet to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the pet
     * @return a String containing the name of the pet
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the pet
     * @param name a String containing the name of the pet to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the category of the pet
     * @return a Category object containing the category of the pet
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the pet
     * @param category a Category object containing the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns the photo URLs of the pet
     * @return a List of Strings containing the photo URLs of the pet
     */
    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    /**
     * Sets the photo URLs of the pet
     * @param photoUrls a List of Strings containing the photo URLs of the pet to set
     */
    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    /**
     * Returns the tags of the pet
     * @return a List of Tags containing the tags of the pet
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the tags of the pet
     * @param tags a List of Tags containing the tags of the pet to set
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Returns the status of the pet in the store
     * @return a String containing the status of the pet in the store
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the pet in the store
     * @param status a String containing the status of the pet in the store to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Pet { " + "id = " + id + ", category = " + category + ", name = \"" + name +  "\", photoUrls = " +
                photoUrls + ", tags = " + tags + ", status = \"" + status + "\" }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Pet) {
            Pet pet = (Pet) obj;
            return this.getId().equals(pet.getId()) &&
                    this.getCategory().equals(pet.getCategory()) &&
                    this.getName().equals(pet.getName()) &&
                    (this.getPhotoUrls().size() == pet.getPhotoUrls().size()) &&
                    this.getPhotoUrls().containsAll(pet.getPhotoUrls()) &&
                    pet.getPhotoUrls().containsAll(this.getPhotoUrls()) &&
                    (this.getTags().size() == pet.getTags().size()) &&
                    this.getTags().containsAll(pet.getTags()) &&
                    pet.getTags().containsAll(this.getTags()) &&
                    this.getStatus().equals(pet.getStatus());
        }

        return false;
    }
}
