package com.shurik16.SpringVaadin.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
@Table(name = "TTEST")
public class Ttest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    private Date data_test;

    @Column(name = "id_working")
    private int workingid;

    private String result;

    private String tnote;

    public Ttest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getData_test() {
        Instant instant = Instant.ofEpochMilli(data_test.getTime());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public Date getData_testt() {
        return data_test;
    }

    public void setData_test(Date data_test) {
        this.data_test = data_test;
    }

    public void setData_test(LocalDateTime data_test) {
        Instant instant = data_test.toInstant(ZoneOffset.UTC);
        this.data_test = Date.from(instant);
    }

    public int getWorkingid() {
        return workingid;
    }

    public void setWorkingid(int workingid) {
        this.workingid = workingid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTnote() {
        return tnote;
    }

    public void setTnote(String tnote) {
        this.tnote = tnote;
    }

    @Override
    public String toString() {
        return "Ttest{" +
                "id=" + id +
                ", data_test=" + data_test +
                ", workingid=" + workingid +
                ", result='" + result + '\'' +
                ", tnote='" + tnote + '\'' +
                '}';
    }
}
