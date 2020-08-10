package com.shurik16.SpringVaadin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TSTANDING")
public class Tstanding implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    private String name_standing;

    public Tstanding() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_standing() {
        return name_standing;
    }

    public void setName_standing(String name_standing) {
        this.name_standing = name_standing;
    }

    @Override
    public String toString() {
        return "Tstanding{" +
                "id=" + id +
                ", name_standing='" + name_standing + '\'' +
                '}';
    }
}
