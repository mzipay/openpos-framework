package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.List;


public class ItemListScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    private List<IItem> items = new ArrayList<>();
    private SelectionMode selectionMode = SelectionMode.None;
    private int selectedIndex = -1;
    private String itemActionName = "Item";
    private String text;
    private List<MenuItem> itemActions = new ArrayList<>();

    public ItemListScreen() {
        setType(ScreenType.ItemList);
    }
    
    
    public List<IItem> getItems() {
        return items;
    }
    
    public void setItems(List<IItem> items) {
        this.items = items;
    }
    
    public void addItem(IItem item) {
        this.getItems().add(item);
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
 
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }
    
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void setSelectedIndex(int selectedIndex) {
       this.selectedIndex = selectedIndex;
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    public String getItemActionName() {
        return itemActionName;
    }


    public void setItemActionName(String itemActionName) {
        this.itemActionName = itemActionName;
    }

    public List<MenuItem> getItemActions() {
        return itemActions;
    }


    public void setItemActions(List<MenuItem> itemActions) {
        this.itemActions = itemActions;
    }

    public void addItemAction(MenuItem itemAction) {
        this.getItemActions().add(itemAction);
    }

    
}
