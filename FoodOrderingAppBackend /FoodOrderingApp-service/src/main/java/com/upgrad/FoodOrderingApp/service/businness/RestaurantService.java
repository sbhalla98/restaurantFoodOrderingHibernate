package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {


    @Autowired
    private CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantList(String name) throws RestaurantNotFoundException {

        if(name.trim().isEmpty()){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }
        List<RestaurantEntity> list = customerDao.getRestaurantList(name);
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantLists(String name) throws CategoryNotFoundException {

        if(name.trim().isEmpty()){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        CategoryEntity categoryEntity=customerDao.getCategory(name);
        if(categoryEntity==null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        List<RestaurantcategoryEntity> restaurantcategoryEntities=customerDao.getRestaurants(categoryEntity);
        List<RestaurantEntity> list = new ArrayList<>();
        for (RestaurantcategoryEntity r: restaurantcategoryEntities) {

            list.add(r.getRestaurant());
        }
        return list;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity getRestaurant(String name) throws RestaurantNotFoundException {

        if(name.trim().isEmpty()){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity=customerDao.getRestaurant(name);
        if(restaurantEntity==null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantcategoryEntity> getRestaurantCategory(RestaurantEntity restaurantEntity) {

        List<RestaurantcategoryEntity> restaurantcategoryEntities=customerDao.getRestaurantCategory(restaurantEntity);
        return restaurantcategoryEntities;
    }
}
