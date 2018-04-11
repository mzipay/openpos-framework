package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.model.CustomerModel;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.screen.AbstractScreen;
import org.jumpmind.pos.core.screen.Customer;
import org.jumpmind.pos.core.screen.CustomerSearchResultsScreen;
import org.jumpmind.pos.core.screen.DynamicFormScreen;

public class CustomerSearchState extends AbstractState {

    @Override
    public void arrive(Action action) {
        System.out.println("Hello");
        stateManager.showScreen(buildCustomerSearchScreen());
    }
    
    protected AbstractScreen buildCustomerSearchScreen() {
        DynamicFormScreen screen = new DynamicFormScreen();
        screen.getForm().addTextField("emailAddress", "Email Address", "", false);
        screen.getForm().addTextField("phoneNumber", "Phone Number", "", false);
        screen.getForm().addTextField("customerId", "Customer ID", "", false);
        screen.getForm().addTextField("lastName", "Last Name", "", false);
        screen.getForm().addTextField("firstName", "First Name", "", false);
        screen.getForm().addTextField("postalCode", "Postal Code", "", false);
        
        return screen;
    }
    
    @ActionHandler
    protected void onNext(Action action) {
        System.out.println("action: " + action);
        Form form = (Form) action.getData();
        
        CustomerModel customer = new CustomerModel();
        
        customer.setCustomerId(form.getFormElementValue("customerId"));
        customer.setFirstName(form.getFormElementValue("firstName"));
        customer.setLastName(form.getFormElementValue("lastName"));
        
        Action customerSelectedAction = new Action();
        customerSelectedAction.setName("CustomerSearchResultsLoaded");
        customerSelectedAction.setData(customer);
        stateManager.doAction(customerSelectedAction);
    }
}
