package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Base64;
import java.util.UUID;
import java.lang.String;

@RestController
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST,path = "/customer/signup",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException

    {

        final CustomerEntity customerEntity=new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setSalt("1234abc");

        final CustomerEntity createdCustomerEntity = customerService.signup(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);

    }
    @RequestMapping(method = RequestMethod.POST,path = "/customer/login",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode;
        try {
            decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        }
        catch (Exception e){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        CustomerAuthTokenEntity customerAuthTokenEntity = customerService.authenticate(decodedArray[0], decodedArray[1]);
        CustomerEntity customerEntity = customerAuthTokenEntity.getCustomer();

        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid())
                .firstName(customerEntity.getFirstName()).lastName(customerEntity.getLastName())
                .emailAddress(customerEntity.getEmail()).contactNumber(customerEntity.getContactNumber()).message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthTokenEntity.getAccessToken());
        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST,path = "/customer/logout",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        CustomerEntity customerEntity;

        String acesstoken;
        try{
            acesstoken = authorization.split("Bearer")[1];
        }
        catch (Exception e){
            throw new AuthorizationFailedException("ATHR-004","Incorrect format of decoded customer name and password send it with Bearer");
        }
        customerEntity = customerService.authorize(acesstoken.trim());

        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY ");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT,path = "/customer/password",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest,@RequestHeader("authorization") final String authorization) throws UpdateCustomerException,AuthorizationFailedException {
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse();
        CustomerEntity customerEntity;
        String acesstoken;
        try{
            acesstoken = authorization.split("Bearer")[1];
        }
        catch (Exception e){
            throw new AuthorizationFailedException("ATHR-004","Incorrect format of decoded customer name and password send it with Bearer");
        }
        customerEntity = customerService.updatePassword(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword(),acesstoken.trim());


        updatePasswordResponse.id(customerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }
}
