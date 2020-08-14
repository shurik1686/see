package com.shurik16.SpringVaadin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
@Table(name = "TMAN")
public class Tman implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String firstname;
    private String lastname;
    private String middlename;
    private Date date_of_birth = new Date();
    private String town_b;
    private String reg_address;
    private String residence_address;
    private String phone;
    private String foto;
    // private int posit;
    // @NotNull

    @ManyToOne//(optional = false/*,fetch = FetchType.LAZY*/, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_standing")
    private Tstanding standing;

    @ManyToOne//(optional = false/*,fetch = FetchType.LAZY*/, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_otdel")
    private Totdel otdel;

    @ManyToOne//(optional = false/*,fetch = FetchType.LAZY*/, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_posit")
    private Tposit posit;

    @ManyToOne//(optional = false/*,fetch = FetchType.LAZY*/, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_company")
    private Tcompany company;

    private int id_foto;

    public Tman() {
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int idfoto) {
        this.id_foto = idfoto;
    }

    public Tposit getPosit() {
        return posit;
    }

    public void setPosit(Tposit posit) {
        this.posit = posit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public LocalDateTime getDate_of_birth() {
        Instant instant = Instant.ofEpochMilli(date_of_birth.getTime());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setDate_of_birth(LocalDateTime date_of_birth) {
        Instant instant = date_of_birth.toInstant(ZoneOffset.UTC);
        this.date_of_birth = Date.from(instant);
    }

    public String getTown_b() {
        return town_b;
    }

    public void setTown_b(String town_b) {
        this.town_b = town_b;
    }

    public String getReg_address() {
        return reg_address;
    }

    public void setReg_address(String reg_address) {
        this.reg_address = reg_address;
    }

    public String getResidence_address() {
        return residence_address;
    }

    public void setResidence_address(String residence_address) {
        this.residence_address = residence_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Tcompany getCompany() {
        return company;
    }

    public void setCompany(Tcompany company) {
        this.company = company;
    }

    public Totdel getOtdel() {
        return otdel;
    }

    public void setOtdel(Totdel otdel) {
        this.otdel = otdel;
    }

    public Tstanding getStanding() {
        return standing;
    }

    public void setStanding(Tstanding standing) {
        this.standing = standing;
    }

    @Override
    public String toString() {
        return "Tman{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", town_b='" + town_b + '\'' +
                ", reg_address='" + reg_address + '\'' +
                ", residence_address='" + residence_address + '\'' +
                ", phone='" + phone + '\'' +
                ", foto='" + foto + '\'' +
                ", standing=" + standing.getName_standing() +
                ", otdel=" + otdel.getName_otdel() +
                ", posit=" + posit.getName_position() +
                ", company=" + company.getName_company() +
                '}';
    }
}
