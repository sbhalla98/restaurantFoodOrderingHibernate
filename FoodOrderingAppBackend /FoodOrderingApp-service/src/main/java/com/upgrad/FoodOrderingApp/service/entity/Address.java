package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address",schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "addressbyuuid", query = "select u from Address u where u.uuid = :uuid")
        }
)
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    @NotNull
    private String uuid;


    @Column(name = "flat_buil_number")
    @NotNull
    private String flatbuilnumber;


    @Column(name = "locality")
    @NotNull
    private String locality;


    @Column(name = "city")
    @NotNull
    private String city;


    @Column(name = "pincode")
    @NotNull
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatbuilnumber() {
        return flatbuilnumber;
    }

    public void setFlatbuilnumber(String flatbuilnumber) {
        this.flatbuilnumber = flatbuilnumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
