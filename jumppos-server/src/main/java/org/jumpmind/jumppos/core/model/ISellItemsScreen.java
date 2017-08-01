package org.jumpmind.jumppos.core.model;

import java.util.List;

import org.jumpmind.jumppos.pos.state.model.PosLineItem;

public interface ISellItemsScreen extends IScreen {

    public List<PosLineItem> getItems();
    public void setItems(List<PosLineItem> items);
    
}
