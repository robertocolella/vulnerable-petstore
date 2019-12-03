package io.swagger.injectablepetstore.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = null;

    @Column(name = "name")
    private String name = null;

    public Tag() {}

    public Tag(String name) {
        this.id = 0L;
        this.name = name;
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of the tag
     * @return a Long containing the id of the tag
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the tag
     * @param id a Long containing the id of the tag to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the tag
     * @return a String containing the name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag
     * @param name a String containing the name of the tag to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Tag) {
            Tag tag = (Tag) obj;
            return this.getId().equals(tag.getId()) &&
                    this.getName().equals(tag.getName());
        }

        return false;
    }

    @Override
    public String toString() {
        return "Tag { " + "id = " + id + ", name = \"" + name + "\" }";
    }
}
