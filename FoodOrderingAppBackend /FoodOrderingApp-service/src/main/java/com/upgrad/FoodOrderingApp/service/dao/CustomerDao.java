package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomerDao {


    @PersistenceContext
    private EntityManager entityManager;


    public CustomerEntity createUser(CustomerEntity customerEntity) {

            List<CustomerEntity> c = entityManager.createNamedQuery("userBymobile", CustomerEntity.class).setParameter("contactNumber", customerEntity.getContactNumber()).getResultList();
            if(!(c.isEmpty())){
                return null;
            }
            entityManager.persist(customerEntity);
            return customerEntity;

    }


    public CustomerEntity getUserByEmail(final String username) {
        try {
            return entityManager.createNamedQuery("userBymobile", CustomerEntity.class).setParameter("contactNumber", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Address adressSave(final Address address) {
        entityManager.persist(address);
        return address;
    }
    public CustomerAddress customerAdressSave(final CustomerAddress customeraddress) {
        entityManager.persist(customeraddress);
        return customeraddress;
    }

    public CustomerAuthTokenEntity createAuthToken(final CustomerAuthTokenEntity customerAuthTokenEntity) {
        entityManager.persist(customerAuthTokenEntity);
        return customerAuthTokenEntity;
    }

    public CustomerAuthTokenEntity getUserByacesstoken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userByAcesstoken", CustomerAuthTokenEntity.class).setParameter("accesstoken", accesstoken).getSingleResult();
        }
        catch (Exception e){
            return null;
        }

    }

    public State getSateByUuid(final String stateuuid) {
    try{
        return entityManager.createNamedQuery("stateByUuid", State.class).setParameter("uuid", stateuuid).getSingleResult();
    }
    catch (Exception e){
        return null;
    }

    }


    public Address getAddress(final String address) {
        try{
            return entityManager.createNamedQuery("addressbyuuid", Address.class).setParameter("uuid", address).getSingleResult();
        }
        catch (Exception e){
            return null;
        }

    }
    public CategoryEntity getCategory(final String name) {
        try{
            return entityManager.createNamedQuery("categorybyuuid", CategoryEntity.class).setParameter("uuid", name).getSingleResult();
        }
        catch (Exception e){
            return null;
        }

    }

    public CustomerAddress getCustomer(Address address) {
        return entityManager.createNamedQuery("customerbyaddress", CustomerAddress.class).setParameter("id", address).getSingleResult();

    }

    public void updateUser(final CustomerEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public void deletegivenaddress(Address address) {
        entityManager.remove(address);
    }

    public void updateAuth(final CustomerAuthTokenEntity customerAuthTokenEntity) {
        entityManager.merge(customerAuthTokenEntity);
    }

    public List<CustomerAddress> getList(CustomerEntity customerEntity) {
        List<CustomerAddress> list = entityManager.createNamedQuery("getList", CustomerAddress.class).setParameter("id", customerEntity).getResultList();
        return list;
    }

    public List<RestaurantEntity> getRestaurantList(String name)  {
        name = name.toLowerCase();
        List<RestaurantEntity> list = entityManager.createNamedQuery("getRestaurantList", RestaurantEntity.class).setParameter("name", "%"+name+"%").getResultList();
        return list;
    }

    public List<RestaurantcategoryEntity> getRestaurants(CategoryEntity categoryEntity)  {
        List<RestaurantcategoryEntity> list = entityManager.createNamedQuery("yourList", RestaurantcategoryEntity.class).setParameter("id",categoryEntity).getResultList();
        return list;
    }

    public List<RestaurantcategoryEntity> getRestaurantCategory(RestaurantEntity restaurantEntity)  {
        List<RestaurantcategoryEntity> list = entityManager.createNamedQuery("yourListofcategory", RestaurantcategoryEntity.class).setParameter("id",restaurantEntity).getResultList();
        return list;
    }

    public RestaurantEntity getRestaurant(String name)  {
        try {
            return entityManager.createNamedQuery("getRestaurant", RestaurantEntity.class).setParameter("id", name).getSingleResult();
        }
        catch (Exception e){
            return null;
        }

    }


    public List<CategoryEntity> getcategories()  {
        List<CategoryEntity> list = entityManager.createNamedQuery("listofcategories", CategoryEntity.class).getResultList();
        return list;
    }

    public List<CategoryItemEntity> getItems(CategoryEntity categoryEntity)  {
        List<CategoryItemEntity> list = entityManager.createNamedQuery("listofItems", CategoryItemEntity.class).setParameter("id",categoryEntity).getResultList();
        return list;
    }
}
