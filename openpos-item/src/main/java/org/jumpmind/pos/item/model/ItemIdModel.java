package org.jumpmind.pos.item.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="item_id")
public class ItemIdModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey=true)
    String itemId;
    
    @Column(primaryKey=true)
    String posItemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPosItemId() {
        return posItemId;
    }

    public void setPosItemId(String posItemId) {
        this.posItemId = posItemId;
    }
    
}
