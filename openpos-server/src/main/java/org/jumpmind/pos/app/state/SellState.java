package org.jumpmind.pos.app.state;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.SellItemScreen;

public class SellState extends AbstractSecureState {

    
    @Override
    protected void secureArrive() {
        stateManager.showScreen(buildScreen());
    }

    protected DefaultScreen buildScreen() {
        SellItemScreen screen = new SellItemScreen();
        screen.setBackButton(new MenuItem("Back", "Back", true));
        return screen;
    }
}
