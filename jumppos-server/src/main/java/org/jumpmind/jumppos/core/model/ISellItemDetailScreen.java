package org.jumpmind.jumppos.core.model;

import java.util.List;

import org.jumpmind.jumppos.pos.state.model.PosLineItem;

public interface ISellItemDetailScreen extends IScreen {
    // TODO: Maybe just needs to take an individual item?
    public List<PosLineItem> getItems();
    public void setItems(List<PosLineItem> items);

}
