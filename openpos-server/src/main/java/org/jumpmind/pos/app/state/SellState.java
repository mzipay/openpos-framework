package org.jumpmind.pos.app.state;

import org.jumpmind.pos.core.screen.SellScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.SellItemScreen;
import org.jumpmind.pos.core.screen.SellScreen.ScanType;

public class SellState extends AbstractSecureState {

    
    @Override
    protected void secureArrive() {
        stateManager.showScreen(buildScreen());
    }

    protected SellScreen buildScreen() {
        SellItemScreen screen = new SellItemScreen();
        screen.setLogoutButton(new MenuItem("Back", "Logout", "exit_to_app"));
        screen.setPrompt("Ready to begin");
        screen.setTemplate(SellScreen.TEMPLATE_SELL);
        screen.setName("Sell");        
        enableScan(screen);
        screen.addLocalMenuItem(new MenuItem("Customer", "Customer", "person"));
        screen.addLocalMenuItem(new MenuItem("Returns", "Returns", "receipt"));
        return screen;
    }
    
    protected void enableScan(SellItemScreen screen) {
        screen.setShowScan(true);
        screen.setScanType(ScanType.CAMERA_CORDOVA);
        screen.setScanActionName("Scan");
    }
}
