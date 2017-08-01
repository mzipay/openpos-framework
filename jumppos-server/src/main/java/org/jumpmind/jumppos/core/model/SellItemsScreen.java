package org.jumpmind.jumppos.core.model;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.pos.state.model.PosLineItem;

public abstract class SellItemsScreen implements ISellItemsScreen {
    List<PosLineItem> items = new ArrayList<>();

    public List<PosLineItem> getItems() {
        return items;
    }

    public void setItems(List<PosLineItem> items) {
        this.items = items;
    }

}
