package com.shurik16.SpringVaadin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity

@Table(name = "TSTATUS_HISTORY")
public class HistoryMan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Tstanding status;

    private int mankey;

    @ManyToOne
    @JoinColumn(name = "id_manager")
    private User managerkey;

    private Date datestatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tstanding getStatus() {
        return status;
    }

    public void setStatus(Tstanding status) {
        this.status = status;
    }

    public int getMankey(int i) {
        return mankey;
    }

    public void setMankey(int mankey) {
        this.mankey = mankey;
    }

    public User getManagerkey() {
        return managerkey;
    }

    public void setManagerkey(User managerkey) {
        this.managerkey = managerkey;
    }

    public Date getDatestatus() {
        return datestatus;
    }

    public void setDatestatus(Date datestatus) {
        this.datestatus = datestatus;
    }

    @Override
    public String toString() {
        return "HistoryMan{" +
                "id=" + id +
                ", status=" + status +
                ", id_man=" + mankey +
                ", id_manager=" + managerkey +
                ", dateStatus=" + datestatus +
                '}';
    }
}
