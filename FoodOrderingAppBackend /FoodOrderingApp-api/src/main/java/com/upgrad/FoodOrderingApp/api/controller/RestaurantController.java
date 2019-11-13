package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/name/{reastaurant_name}")
    public ResponseEntity<RestaurantListResponse> getRestaurantList(@PathVariable("reastaurant_name") final String restaurantName) throws RestaurantNotFoundException {
        List<RestaurantEntity> restaurantEntities = restaurantService.getRestaurantList(restaurantName);
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity l:restaurantEntities) {

            RestaurantList restaurantList=new RestaurantList();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress=new RestaurantDetailsResponseAddress();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState=new RestaurantDetailsResponseAddressState();

            restaurantDetailsResponseAddress.city(l.getAddress().getCity());
            restaurantDetailsResponseAddress.id(UUID.fromString(l.getAddress().getUuid()));
            restaurantDetailsResponseAddress.locality(l.getAddress().getLocality());
            restaurantDetailsResponseAddress.flatBuildingName(l.getAddress().getFlatbuilnumber());
            restaurantDetailsResponseAddress.pincode(l.getAddress().getPincode());
            restaurantDetailsResponseAddressState.stateName(l.getAddress().getState().getStatename());
            restaurantDetailsResponseAddressState.id(UUID.fromString(l.getAddress().getState().getUuid()));
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantList.id(UUID.fromString(l.getUuid()));
            restaurantList.averagePrice(l.getAvgprice());
            restaurantList.photoURL(l.getPhotourl());
            restaurantList.restaurantName(l.getRestaurantName());
            restaurantList.customerRating(l.getCustomerRating());
            restaurantList.numberCustomersRated(l.getCustomersrated());
            restaurantList.address(restaurantDetailsResponseAddress);
            restaurantList.categories("Indian");
            restaurantLists.add(restaurantList);
        }

    RestaurantListResponse restaurantListResponse=new RestaurantListResponse();
        restaurantListResponse.restaurants(restaurantLists);
    return new  ResponseEntity<RestaurantListResponse>(restaurantListResponse,HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/category/{category_id}")
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategory(@PathVariable("category_id") final String categoryid) throws CategoryNotFoundException {
        List<RestaurantEntity> restaurantEntities = restaurantService.getRestaurantLists(categoryid);
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity l:restaurantEntities) {

            RestaurantList restaurantList=new RestaurantList();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress=new RestaurantDetailsResponseAddress();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState=new RestaurantDetailsResponseAddressState();

            restaurantDetailsResponseAddress.city(l.getAddress().getCity());
            restaurantDetailsResponseAddress.id(UUID.fromString(l.getAddress().getUuid()));
            restaurantDetailsResponseAddress.locality(l.getAddress().getLocality());
            restaurantDetailsResponseAddress.flatBuildingName(l.getAddress().getFlatbuilnumber());
            restaurantDetailsResponseAddress.pincode(l.getAddress().getPincode());
            restaurantDetailsResponseAddressState.stateName(l.getAddress().getState().getStatename());
            restaurantDetailsResponseAddressState.id(UUID.fromString(l.getAddress().getState().getUuid()));
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantList.id(UUID.fromString(l.getUuid()));
            restaurantList.averagePrice(l.getAvgprice());
            restaurantList.photoURL(l.getPhotourl());
            restaurantList.restaurantName(l.getRestaurantName());
            restaurantList.customerRating(l.getCustomerRating());
            restaurantList.numberCustomersRated(l.getCustomersrated());
            restaurantList.address(restaurantDetailsResponseAddress);
            restaurantList.categories("Indian");
            restaurantLists.add(restaurantList);
        }
        RestaurantListResponse restaurantListResponse=new RestaurantListResponse();
        restaurantListResponse.restaurants(restaurantLists);
        return new  ResponseEntity<RestaurantListResponse>(restaurantListResponse,HttpStatus.OK);

    }



    @RequestMapping(method = RequestMethod.GET,path = "/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByUuid(@PathVariable("restaurant_id") final String restaurantid) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntities = restaurantService.getRestaurant(restaurantid);
        RestaurantDetailsResponse restaurantLists = new RestaurantDetailsResponse();

            RestaurantList restaurantList=new RestaurantList();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress=new RestaurantDetailsResponseAddress();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState=new RestaurantDetailsResponseAddressState();

            restaurantDetailsResponseAddress.city(restaurantEntities.getAddress().getCity());
            restaurantDetailsResponseAddress.id(UUID.fromString(restaurantEntities.getAddress().getUuid()));
            restaurantDetailsResponseAddress.locality(restaurantEntities.getAddress().getLocality());
            restaurantDetailsResponseAddress.flatBuildingName(restaurantEntities.getAddress().getFlatbuilnumber());
            restaurantDetailsResponseAddress.pincode(restaurantEntities.getAddress().getPincode());
            restaurantDetailsResponseAddressState.stateName(restaurantEntities.getAddress().getState().getStatename());
            restaurantDetailsResponseAddressState.id(UUID.fromString(restaurantEntities.getAddress().getState().getUuid()));
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantLists.id(UUID.fromString(restaurantEntities.getUuid()));
            restaurantLists.averagePrice(restaurantEntities.getAvgprice());
            restaurantLists.photoURL(restaurantEntities.getPhotourl());
            restaurantLists.restaurantName(restaurantEntities.getRestaurantName());
            restaurantLists.customerRating(restaurantEntities.getCustomerRating());
            restaurantLists.numberCustomersRated(restaurantEntities.getCustomersrated());
            restaurantLists.address(restaurantDetailsResponseAddress);

            List<RestaurantcategoryEntity>  restaurantcategoryEntities = restaurantService.getRestaurantCategory(restaurantEntities);
            List<CategoryList> categoryLists = new ArrayList<>();
        for (RestaurantcategoryEntity r:restaurantcategoryEntities) {
            CategoryList categoryList=new CategoryList();
            categoryList.categoryName(r.getCategory().getCategoryName());
            categoryList.id(UUID.fromString(r.getCategory().getUuid()));

            List<CategoryItemEntity> categoryItemEntities=categoryService.getItemLists(r.getCategory());
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
            categoryList.itemList(itemLists);
            categoryLists.add(categoryList);
        }
        restaurantLists.categories(categoryLists);

        return new  ResponseEntity<RestaurantDetailsResponse>(restaurantLists,HttpStatus.OK);

    }
}
