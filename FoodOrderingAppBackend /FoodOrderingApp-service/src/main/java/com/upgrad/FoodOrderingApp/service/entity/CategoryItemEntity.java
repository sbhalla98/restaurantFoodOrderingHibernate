package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;

@Entity
@Table(name = "category_item",schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "listofItems", query = "select u from CategoryItemEntity u where u.category = :id order by u.item.itemnames")
        }
)
public class CategoryItemEntity {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
