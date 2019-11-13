package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category",schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "categorybyuuid", query = "select u from CategoryEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "listofcategories", query = "select u from CategoryEntity u order by u.categoryName")
        }
)
public class CategoryEntity {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "category_name")
    @NotNull
    private String categoryName;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
