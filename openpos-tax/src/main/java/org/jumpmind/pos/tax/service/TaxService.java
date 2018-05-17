package org.jumpmind.pos.tax.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.jumpmind.pos.tax.model.RetailTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tax")
public class TaxService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;

    @RequestMapping("/calculateTax")
    public void calculateTax(RetailTransaction trans) {
        endpointDispatcher.dispatch("/calculateTax", trans);
    }

}
