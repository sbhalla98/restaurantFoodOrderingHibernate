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
public class CategoryService {

    @Autowired
    private CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getCategories()  {
        List<CategoryEntity> list = customerDao.getcategories();
        return list;

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategory(String name)  throws CategoryNotFoundException {
        if(name.trim().isEmpty()){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        CategoryEntity categoryEntity=customerDao.getCategory(name);
        if(categoryEntity==null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return categoryEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryItemEntity> getItemLists(CategoryEntity categoryEntity)   {
        List<CategoryItemEntity> categoryItemEntities=customerDao.getItems(categoryEntity);
        return categoryItemEntities;
    }
}
