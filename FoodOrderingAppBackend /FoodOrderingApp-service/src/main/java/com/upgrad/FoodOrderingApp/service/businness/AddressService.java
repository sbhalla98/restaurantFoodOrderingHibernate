package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class AddressService {

    @Autowired
    private CustomerDao customerDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity authorize(final String authorize) throws AuthorizationFailedException {

        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getUserByacesstoken(authorize);
        if (customerAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if (customerAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        }
        if(customerAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
        throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint");
        }
        return customerAuthTokenEntity.getCustomer();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Address saveAddress(String city, String locality, String pincode, String flatbuilnumber, String stateuuid,CustomerEntity customerEntity) throws SaveAddressException, AddressNotFoundException {
        if(city.trim().isEmpty() || locality.trim().isEmpty() || pincode.trim().isEmpty() || stateuuid.isEmpty()){
            throw new SaveAddressException("SAR-001","No field can be empty");
        }
        if(pincode.trim().length() < 6 || !pincode.matches("[0-9]+")){
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }
        State state = customerDao.getSateByUuid(stateuuid);
        if(state==null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }

        final Address address= new Address();
        address.setUuid(UUID.randomUUID().toString());
        address.setCity(city);
        address.setLocality(locality);
        address.setFlatbuilnumber(flatbuilnumber);
        address.setPincode(pincode);
        address.setState(state);
        Address address1=customerDao.adressSave(address);


        final CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setCustomer(customerEntity);
        customerAddress.setAddress(address);
        CustomerAddress customerAddress1=customerDao.customerAdressSave(customerAddress);

        return address1;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Address> getListOfAddresses(CustomerEntity customerEntity)  {
       List<CustomerAddress> list = customerDao.getList(customerEntity);

       List<Address> list1 = new ArrayList<Address>();
        for (CustomerAddress c:list) {
            list1.add(c.getAddress());
        }
       return list1;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteaddress(final String  addressuuid,CustomerEntity customerEntity)  throws AddressNotFoundException {
        if(addressuuid.trim().isEmpty()){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        Address address=customerDao.getAddress(addressuuid);
        if(address==null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        CustomerAddress customerAddress=customerDao.getCustomer(address);
        if(customerAddress.getCustomer().getId()!=customerEntity.getId()){
            throw new AddressNotFoundException("ANF-004","You are not authorized to view/update/delete any one else's address");
        }
        customerDao.deletegivenaddress(address);

    }



}
