package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.model.FormField;

public class TenderingScreen extends DefaultScreen {
    private static final long serialVersionUID = 1L;

    private List<IItem> tenderItems = new ArrayList<>();
    private SelectionMode selectionMode = SelectionMode.Single;
    private String text;
    private FormField tenderAmount;
    private String balanceDue;
    private List<MenuItem> itemActions = new ArrayList<>();
    
    public TenderingScreen() {
        setType(ScreenType.Tendering);
    }
    
    public List<IItem> getTenderItems() {
        return tenderItems;
    }
    public void setTenderItems(List<IItem> tenderItems) {
        this.tenderItems = tenderItems;
    }
    public void addTenderItem(IItem tenderItem) {
        this.tenderItems.add(tenderItem);
    }
    
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public FormField getTenderAmount() {
        return tenderAmount;
    }
    public void setTenderAmount(FormField tenderAmount) {
        this.tenderAmount = tenderAmount;
    }
    public String getBalanceDue() {
        return balanceDue;
    }
    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }
    public List<MenuItem> getItemActions() {
        return itemActions;
    }
    public void setItemActions(List<MenuItem> itemActions) {
        this.itemActions = itemActions;
    }
    
    
}
