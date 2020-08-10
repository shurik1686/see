package com.shurik16.SpringVaadin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TPOSIT")
public class Tposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    private String name_position;


    public Tposit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_position() {
        return name_position;
    }

    public void setName_position(String name_position) {
        this.name_position = name_position;
    }

    @Override
    public String toString() {
        return "Tposit{" +
                "id=" + id +
                ", name_position='" + name_position + '\'' +
                '}';
    }
}
