package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "restaurant",schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "getRestaurantList", query = "select u from RestaurantEntity u where lower(u.restaurantName) LIKE :name order by lower(u.restaurantName)"),
                @NamedQuery(name = "getRestaurant", query = "select u from RestaurantEntity u where u.uuid = :id")
        }
)
public class RestaurantEntity {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "restaurant_name")
    @NotNull
    private String restaurantName;

    @Column(name = "photo_url")
    @NotNull
    private String photourl;

    @Column(name = "customer_rating")
    @NotNull
    private BigDecimal customerRating;

    @Column(name = "average_price_for_two")
    @NotNull
    private Integer avgprice;

    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer customersrated;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public BigDecimal getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(BigDecimal customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(Integer avgprice) {
        this.avgprice = avgprice;
    }

    public Integer getCustomersrated() {
        return customersrated;
    }

    public void setCustomersrated(Integer customersrated) {
        this.customersrated = customersrated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
