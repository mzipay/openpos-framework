package org.jumpmind.jumppos.pos.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.screen.DefaultScreen;
import org.jumpmind.jumppos.core.screen.MenuItem;

public class SellItemDetailScreen extends DefaultScreen {

    private PosLineItem item = new PosLineItem();

    private List<MenuItem> itemActions = new ArrayList<>();

    public SellItemDetailScreen() {
        setType(SELL_ITEM_DETAIL_SCREEN_TYPE);
    }

    public List<MenuItem> getItemActions() {
        return itemActions;
    }

    public void setItemActions(List<MenuItem> itemActions) {
        this.itemActions = itemActions;
    }

    public PosLineItem getItem() {
        return item;
    }

    public void setItem(PosLineItem item) {
        this.item = item;
    }

}
