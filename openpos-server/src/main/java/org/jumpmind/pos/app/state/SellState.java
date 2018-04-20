package org.jumpmind.pos.app.state;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.SellItemScreen;
import org.jumpmind.pos.core.screen.SellScreen;
import org.jumpmind.pos.core.screen.SellScreen.ScanType;
import org.jumpmind.pos.core.template.SellTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class SellState implements IState {

    @In(scope=ScopeType.Node)
    IStateManager stateManager;
    
    @Override
    public void arrive(Action action) {
        stateManager.showScreen(buildScreen());
    }

    protected SellScreen buildScreen() {
        SellItemScreen screen = new SellItemScreen();
        screen.setLogoutButton(new MenuItem("Back", "Logout", "exit_to_app"));
        screen.setPrompt("Ready to begin");
        screen.setTemplate(new SellTemplate());
        screen.setName("Sell");        
        enableScan(screen);
        screen.addLocalMenuItem(new MenuItem("CustomerSearch", "Customer", "person"));
        screen.addLocalMenuItem(new MenuItem("Returns", "Returns", "receipt"));
        screen.addLocalMenuItem(new MenuItem("Foo", "Invalid Action", "receipt"));
        return screen;
    }
    
    protected void enableScan(SellItemScreen screen) {
        screen.setShowScan(true);
        screen.setScanType(ScanType.CAMERA_CORDOVA);
        screen.setScanActionName("Scan");
    }
    
//    @ActionHandler
//    protected void onCustomerSearch(Action action) {
//        Action subAction = new Action("CustomerSearchSub");
//        subAction.setSubStateCallback((a)->onCustomerSearchComplete(a));
//        stateManager.doAction(subAction);
//    }
    
    @ActionHandler    
    protected void onCustomerSearchComplete(Action action) {
        stateManager.getUI().notify("Got the customer selected " + action.getData(), "CustomerSelectAcknowledged");
    }
    
    @ActionHandler
    protected void onCustomerSelectAcknowledged(Action action) {
        stateManager.showScreen(buildScreen());        
    }
}
