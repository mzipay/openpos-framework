package org.jumpmind.pos.customer.service;

import org.jumpmind.pos.customer.model.CustomerRepository;
import org.jumpmind.pos.customer.model.SearchCustomerResult;
import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager="customerTxManager")
public class SearchCustomerEndpoint {

    @Autowired
    private CustomerRepository customerRepository;

    @Endpoint("/searchCustomer")
    public SearchCustomerResult searchCustomer(
        @RequestParam(value="firstName", defaultValue="") String firstName, 
        @RequestParam(value="lastName", defaultValue="") String lastName, 
        @RequestParam(value="phone", defaultValue="") String phone, 
        @RequestParam(value="email", defaultValue="") String email, 
        @RequestParam(value="city", defaultValue="") String city,
        @RequestParam(value="state", defaultValue="") String state,
        @RequestParam(value="postalCode", defaultValue="") String postalCode) {
        return null;
    }
}
