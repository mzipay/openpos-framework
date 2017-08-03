package org.jumpmind.jumppos.core.model;

import java.util.List;

import org.jumpmind.jumppos.pos.state.model.PosLineItem;

public interface ISellItemDetailScreen extends IScreen {

    public List<PosLineItem> getItems();
    public void setItems(List<PosLineItem> items);
    public List<MenuItem> getItemActions();    
    public void setItemActions(List<MenuItem> itemActions);

}
