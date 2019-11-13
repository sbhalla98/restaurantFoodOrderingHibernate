package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @RequestMapping(method = RequestMethod.POST,path = "/address",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveaddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest, @RequestHeader("authorization") final String authorization) throws SaveAddressException, AuthorizationFailedException, AddressNotFoundException {


        String acesstoken;
        try{
            acesstoken = authorization.split("Bearer")[1];
        }
        catch (Exception e){
            throw new AuthorizationFailedException("ATHR-004","Incorrect format of decoded customer name and password send it with Bearer");
        }
        CustomerEntity customerEntity= addressService.authorize(acesstoken.trim());

        Address address= addressService.saveAddress(saveAddressRequest.getCity(),saveAddressRequest.getLocality(),saveAddressRequest.getPincode(),saveAddressRequest.getFlatBuildingName(),saveAddressRequest.getStateUuid(),customerEntity);


        SaveAddressResponse saveAddressResponse=new SaveAddressResponse();
        saveAddressResponse.id(address.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new  ResponseEntity<SaveAddressResponse>(saveAddressResponse,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET,path = "/address/customer")
    public ResponseEntity<AddressListResponse> getAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{


        String acesstoken;
        try{
            acesstoken = authorization.split("Bearer")[1];
        }
        catch (Exception e){
            throw new AuthorizationFailedException("ATHR-004","Incorrect format of decoded customer name and password send it with Bearer");
        }
        CustomerEntity customerEntity= addressService.authorize(acesstoken.trim());

        List<Address> list = addressService.getListOfAddresses(customerEntity);

        List<AddressList> responselist = new ArrayList<AddressList>();
        for (Address a:list) {
            AddressList addressList = new AddressList();
            AddressListState addressListState=new AddressListState();
            addressListState.stateName(a.getState().getStatename());
            addressListState.id(UUID.fromString(a.getState().getUuid()));
            addressList.city(a.getCity());
            addressList.flatBuildingName(a.getFlatbuilnumber());
            addressList.locality(a.getLocality());
            addressList.id(UUID.fromString(a.getUuid()));
            addressList.state(addressListState);
            responselist.add(addressList);
        }


        AddressListResponse addressListResponse=new AddressListResponse();
        addressListResponse.addresses(responselist);
        return new  ResponseEntity<AddressListResponse>(addressListResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.DELETE,path = "/address/{address_id}")
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable("address_id") final String addressuuid,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,AddressNotFoundException{


        String acesstoken;
        try{
            acesstoken = authorization.split("Bearer")[1];
        }
        catch (Exception e){
            throw new AuthorizationFailedException("ATHR-004","Incorrect format of decoded customer name and password send it with Bearer");
        }
        CustomerEntity customerEntity= addressService.authorize(acesstoken.trim());

        addressService.deleteaddress(addressuuid,customerEntity);

        DeleteAddressResponse deleteAddressResponse=new DeleteAddressResponse();
        deleteAddressResponse.id(UUID.fromString(addressuuid)).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse,HttpStatus.OK);



    }

}
