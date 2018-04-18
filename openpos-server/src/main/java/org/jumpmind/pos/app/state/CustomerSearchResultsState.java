package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.model.CustomerModel;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.screen.AbstractScreen;
import org.jumpmind.pos.core.screen.Customer;
import org.jumpmind.pos.core.screen.CustomerSearchResultsScreen;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerSearchResultsState extends AbstractState {
    
    @Autowired(required=false)
    private Boolean orderMode;

    @Override
    public void arrive(Action action) {
        stateManager.showScreen(buildCustomerResultScreen((CustomerModel)action.getData()));
    }
    
    private AbstractScreen buildCustomerResultScreen(CustomerModel customer) {
        CustomerSearchResultsScreen customerSearchResults = new CustomerSearchResultsScreen();
        
        customerSearchResults.setSubmitAction("CustomerSelected");
        
        if (orderMode) {
            System.out.print("We are in order mode.");
        }
        
        Customer screenCustomer = new Customer();
        
        screenCustomer.setFirstName((orderMode ? "ORDER MODE: " : "") + customer.getFirstName());
        screenCustomer.setLastName(customer.getLastName());
        screenCustomer.setEmail(customer.getCustomerId());
        screenCustomer.setLoyaltyId(customer.getCustomerId());
        customerSearchResults.addCustomer(screenCustomer);
        
        return customerSearchResults;
    }    
    
    
    @ActionHandler
    protected void onCustomerSelected(Action action) {
        stateManager.setConversationScope("currentCustomer", action.getData());
        Action finishedAction = new Action(Action.SUB_STATE_COMPLETE, action.getData());        
        stateManager.doAction(finishedAction);
    }

}
