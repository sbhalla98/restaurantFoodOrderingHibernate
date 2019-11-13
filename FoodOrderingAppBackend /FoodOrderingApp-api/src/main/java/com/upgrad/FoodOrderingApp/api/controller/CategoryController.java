package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @RequestMapping(method = RequestMethod.GET,path = "/category")
    public ResponseEntity<CategoriesListResponse> getRestaurantByUuid() {
        List<CategoryEntity> categoryEntities=categoryService.getCategories();
        List<CategoryListResponse> list = new ArrayList<>();

        for (CategoryEntity c:categoryEntities) {
            CategoryListResponse categoryListResponse=new CategoryListResponse();
            categoryListResponse.categoryName(c.getCategoryName());
            categoryListResponse.id(UUID.fromString(c.getUuid()));
            list.add(categoryListResponse);
        }
        CategoriesListResponse categoriesListResponse=new CategoriesListResponse();
        categoriesListResponse.categories(list);
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/category/{category_id}")
    public ResponseEntity<CategoryDetailsResponse> getCategoryByUudi(@PathVariable("category_id") final String categoryid) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategory(categoryid);
        CategoryDetailsResponse categoryDetailsResponse=new CategoryDetailsResponse();

        List<CategoryItemEntity> categoryItemEntities=categoryService.getItemLists(categoryEntity);
        List<ItemList> itemLists=new ArrayList<>();
        for (CategoryItemEntity c:categoryItemEntities) {
            ItemList itemList = new ItemList();
            itemList.id(UUID.fromString(c.getItem().getUuid()));
            itemList.price(c.getItem().getPrice());
            itemList.itemName(c.getItem().getItemnames());
            String type=c.getItem().getType();
            if(type.equals("0")){
                itemList.itemType(ItemList.ItemTypeEnum.fromValue("VEG"));
            }
            else {
                itemList.itemType(ItemList.ItemTypeEnum.fromValue("NON_VEG"));
            }
            itemLists.add(itemList);
        }

        categoryDetailsResponse.categoryName(categoryEntity.getCategoryName());
        categoryDetailsResponse.id(UUID.fromString(categoryEntity.getUuid()));
        categoryDetailsResponse.itemList(itemLists);
        return new  ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse,HttpStatus.OK);

    }

}
