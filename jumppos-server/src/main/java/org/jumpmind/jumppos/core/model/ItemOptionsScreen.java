package org.jumpmind.jumppos.core.model;

import java.util.ArrayList;
import java.util.List;

public class ItemOptionsScreen extends DefaultScreen {

    private PosLineItem item = new PosLineItem();

    private List<MenuItem> itemActions = new ArrayList<>();

    public ItemOptionsScreen() {
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
