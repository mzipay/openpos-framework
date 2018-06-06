package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.FormField;
import org.jumpmind.pos.core.model.IFormElement;

public class TenderingScreen extends Screen {
    private static final long serialVersionUID = 1L;

    private List<IItem> tenderItems = new ArrayList<>();
    private SelectionMode selectionMode = SelectionMode.Single;
    private String text;
    private FormField tenderAmount;
    
    private IFormElement balanceDueAmount;
    @Deprecated private String balanceDue;
    private IFormElement totalAmount;
    private List<MenuItem> itemActions = new ArrayList<>();
    private MenuItem actionButton;
    
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
    
    public IFormElement getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(IFormElement totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBalanceDueAmount(IFormElement balanceDueAmount) {
        this.balanceDueAmount = balanceDueAmount;
    }
    
    public IFormElement getBalanceDueAmount() {
        return this.balanceDueAmount;
    }
    
    @Deprecated
    public String getBalanceDue() {
        return balanceDue;
    }
    @Deprecated
    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }
    public List<MenuItem> getItemActions() {
        return itemActions;
    }
    public void setItemActions(List<MenuItem> itemActions) {
        this.itemActions = itemActions;
    }
    public void addItemAction(MenuItem itemAction) {
        this.itemActions.add(itemAction);
    }
    
    public MenuItem getActionButton() {
        return actionButton;
    }

    public void setActionButton(MenuItem actionButton) {
        this.actionButton = actionButton;
    }
    
}
