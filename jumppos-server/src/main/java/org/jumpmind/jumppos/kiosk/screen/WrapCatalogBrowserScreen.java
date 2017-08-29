package org.jumpmind.jumppos.kiosk.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.screen.EmbeddedWebPageScreen;
import org.jumpmind.jumppos.pos.screen.LineItem;

public class WrapCatalogBrowserScreen extends EmbeddedWebPageScreen {

    private static final long serialVersionUID = 1L;
    
    List<LineItem> lineItems = new ArrayList<LineItem>();

    public WrapCatalogBrowserScreen() {
        super();
    }
    
    public WrapCatalogBrowserScreen(String url) {
        super(url);
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
    
    public List<LineItem> getLineItems() {
        return lineItems;
    }

}
