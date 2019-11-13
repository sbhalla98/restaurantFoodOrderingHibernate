package com.upgrad.FoodOrderingApp.service.businness;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException {
        if(customerEntity.getContactNumber().trim().isEmpty() || customerEntity.getEmail().isEmpty() || customerEntity.getFirstName().isEmpty() || customerEntity.getPassword().isEmpty()) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if(!(customerEntity.getEmail().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))){
            throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        }
        if(customerEntity.getContactNumber().length()!=10 || !customerEntity.getContactNumber().matches("[0-9]+")){
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }
        if(customerEntity.getPassword().length()<8 || !customerEntity.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")){
            throw new SignUpRestrictedException("SGR-004","Weak password!");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        CustomerEntity c= customerDao.createUser(customerEntity);
        if(c==null){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number");
        }

        return c;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException{


        CustomerEntity customerEntity = customerDao.getUserByEmail(username);
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthTokenEntity customerAuthTokenEntity = new CustomerAuthTokenEntity();
            customerAuthTokenEntity.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(customerAuthTokenEntity.getUuid(), now, expiresAt));
            customerAuthTokenEntity.setLoginAt(now);
            customerAuthTokenEntity.setExpiresAt(expiresAt);
            customerAuthTokenEntity.setUuid(UUID.randomUUID().toString());


            customerDao.createAuthToken(customerAuthTokenEntity);
            customerDao.updateUser(customerEntity);

            return customerAuthTokenEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }


    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity authorize(final String authorize) throws AuthorizationFailedException {

        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getUserByacesstoken(authorize);
        if (customerAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
//      if(customerAuthTokenEntity.getExpiresAt() < ZonedDateTime.now()){
//        throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
//      }

        customerAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
        customerDao.updateAuth(customerAuthTokenEntity);
        return customerAuthTokenEntity.getCustomer();

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(final String oldPassword,final String newPassword,final String acesstoken) throws UpdateCustomerException,AuthorizationFailedException {
        if(newPassword == "" || oldPassword==""){
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        }
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getUserByacesstoken(acesstoken);
        if (customerAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
//      if(customerAuthTokenEntity.getExpiresAt() < ZonedDateTime.now()){
//        throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
//      }

        if(newPassword.length()<8 || !newPassword.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")){
            throw new UpdateCustomerException("UCR-001","Weak password!");
        }


       String decodeText = passwordCryptographyProvider.encrypt(oldPassword,customerAuthTokenEntity.getCustomer().getSalt());
        if(!decodeText.equals(customerAuthTokenEntity.getCustomer().getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
        CustomerEntity customerEntity = customerAuthTokenEntity.getCustomer();
        String encryptedSalt=encryptedText[0];
        String encryptedPassword = encryptedText[1];
        customerEntity.setSalt(encryptedSalt);
        customerEntity.setPassword(encryptedPassword);
        customerDao.updateUser(customerEntity);

        return customerEntity;

    }


}
