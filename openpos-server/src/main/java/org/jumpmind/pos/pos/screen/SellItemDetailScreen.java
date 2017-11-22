package org.jumpmind.pos.pos.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.ScreenType;
import org.jumpmind.pos.core.screen.SellItem;
import org.jumpmind.pos.core.screen.Transaction;

public class SellItemDetailScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    private SellItem item = new SellItem();
    private List<MenuItem> itemActions = new ArrayList<>();
    private Transaction transaction = new Transaction();

    public SellItemDetailScreen() {
        setType(ScreenType.SellItemDetail);
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
