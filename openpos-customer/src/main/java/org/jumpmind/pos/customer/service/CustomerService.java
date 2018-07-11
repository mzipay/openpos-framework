package org.jumpmind.pos.customer.service;

import org.jumpmind.pos.customer.model.SearchCustomerResult;
import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;

    @RequestMapping("/searchCustomer")
    public SearchCustomerResult searchCustomer(
        @RequestParam(value="deviceId", defaultValue="") String deviceId,
        @RequestParam(value="firstName", defaultValue="") String firstName, 
        @RequestParam(value="lastName", defaultValue="") String lastName, 
        @RequestParam(value="phone", defaultValue="") String phone, 
        @RequestParam(value="email", defaultValue="") String email, 
        @RequestParam(value="city", defaultValue="") String city,
        @RequestParam(value="state", defaultValue="") String state,
        @RequestParam(value="postalCode", defaultValue="") String postalCode) {
        return endpointDispatcher.dispatch("/searchCustomer", deviceId, firstName, lastName, phone, email, city, state, postalCode);
    }
}
