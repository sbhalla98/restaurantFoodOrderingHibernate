package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;

@Entity
@Table(name = "customer_address",schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "getList", query = "select u from CustomerAddress u where u.customer = :id"),
                @NamedQuery(name = "customerbyaddress", query = "select u from CustomerAddress u where u.address = :id")
        }
)
public class CustomerAddress {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
