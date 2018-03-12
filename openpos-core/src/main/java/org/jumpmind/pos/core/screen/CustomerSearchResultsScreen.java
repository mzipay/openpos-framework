package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class CustomerSearchResultsScreen extends SellScreen {

    private static final long serialVersionUID = 1L;
    private String submitAction = "Link";
    
    private List<Customer> customers = new ArrayList<>();
    
    public CustomerSearchResultsScreen() {
		this.setType(ScreenType.CustomerSearch);
	}
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    public void addCustomer(Customer customer) {
    		customers.add(customer);
    }
    
    public void setSubmitAction(String submitAction) {
        this.submitAction = submitAction;
    }
    
    public String getSubmitAction() {
        return submitAction;
    }
}
    