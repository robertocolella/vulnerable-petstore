package io.swagger.injectablepetstore.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = null;

    @Column(name = "name")
    private String name = null;

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of the category
     * @return a Long containing the id of the category
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the category
     * @param id a Long containing the id of the category to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the category
     * @return a String containing the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category
     * @param name a String containing the name of the category to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Category) {
            Category category = (Category) obj;
            return this.getId().equals(category.getId()) &&
                    this.getName().equals(category.getName());
        }

        return false;
    }

    @Override
    public String toString() {
        return "Category { " + "id = " + id + ", name = \"" + name + "\" }";
    }
}
