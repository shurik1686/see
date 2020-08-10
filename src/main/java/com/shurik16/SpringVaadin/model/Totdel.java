package com.shurik16.SpringVaadin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TOTDEL")
public class Totdel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    private String name_otdel;

    public Totdel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_otdel() {
        return name_otdel;
    }

    public void setName_otdel(String name_otdel) {
        this.name_otdel = name_otdel;
    }

    @Override
    public String toString() {
        return "Totdel{" +
                "id=" + id +
                ", name_otdel='" + name_otdel + '\'' +
                '}';
    }
}
