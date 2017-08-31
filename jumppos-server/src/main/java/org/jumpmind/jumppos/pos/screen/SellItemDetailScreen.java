package org.jumpmind.jumppos.pos.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.screen.DefaultScreen;
import org.jumpmind.jumppos.core.screen.SellItem;
import org.jumpmind.jumppos.core.screen.Transaction;
import org.jumpmind.jumppos.core.screen.MenuItem;

public class SellItemDetailScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    private SellItem item = new SellItem();
    private List<MenuItem> itemActions = new ArrayList<>();
    private Transaction transaction = new Transaction();

    public SellItemDetailScreen() {
        setType(SELL_ITEM_DETAIL_SCREEN_TYPE);
    }

    public List<MenuItem> getItemActions() {
        return itemActions;
    }

    public void setItemActions(List<MenuItem> itemActions) {
        this.itemActions = itemActions;
    }

    public SellItem getItem() {
        return item;
    }

    public void setItem(SellItem item) {
        this.item = item;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
