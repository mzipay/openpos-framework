package org.jumpmind.pos.app.state;

import java.util.List;

import org.jumpmind.pos.app.model.CustomerModel;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.screen.AbstractScreen;
import org.jumpmind.pos.core.screen.Customer;
import org.jumpmind.pos.core.screen.CustomerSearchResultsScreen;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerSearchResultsState extends AbstractState {
    
    @In(scope=ScopeType.Config, required=false)
    private Boolean orderMode;
    
    @In(scope=ScopeType.Flow, required=false)
    private List<CustomerModel> customerSearchResults;

    @Override
    public void arrive(Action action) {
        stateManager.showScreen(buildCustomerResultScreen());
    }
    
    private AbstractScreen buildCustomerResultScreen() {
        CustomerSearchResultsScreen customerSearchResultsScreen = new CustomerSearchResultsScreen();
        
        customerSearchResultsScreen.setSubmitAction("CustomerSelected");
        
        for (CustomerModel customerModel : customerSearchResults) {            
            Customer screenCustomer = new Customer();
            
            screenCustomer.setFirstName((orderMode ? "ORDER MODE: " : "") + customerModel.getFirstName());
            screenCustomer.setLastName(customerModel.getLastName());
            screenCustomer.setEmail(customerModel.getCustomerId());
            screenCustomer.setLoyaltyId(customerModel.getCustomerId());
            customerSearchResultsScreen.addCustomer(screenCustomer);
        }

        return customerSearchResultsScreen;
    }    
    
    
//    @ActionHandler
//    protected void onCustomerSelected(Action action) {
//        stateManager.setConversationScope("currentCustomer", action.getData());
//        Action finishedAction = new Action(Action.SUB_STATE_COMPLETE, action.getData());        
//        stateManager.doAction(finishedAction);
//    }

}
