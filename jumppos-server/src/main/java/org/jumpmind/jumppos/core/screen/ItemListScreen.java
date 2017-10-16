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

    static public class Item implements IItem {
        private String id;
        private Integer index;
        private String description;
        private String subtitle;
        private String amount;
        
        public Item() {}
        
        public Item(String description, String subtitle, String amount) {
            this.description = description;
            this.subtitle = subtitle;
            this.amount = amount;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        @Override
        public void setDescription(String description) {
            this.description = description;
        }
        @Override
        public String getSubtitle() {
            return subtitle;
        }
        @Override
        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
        @Override
        public String getAmount() {
            return amount;
        }
        @Override
        public void setAmount(String amount) {
            this.amount = amount;
        }

        @Override
        public Integer getIndex() {
            return this.index;
        }

        @Override
        public void setIndex(Integer index) {
            this.index = index;
        }

        @Override
        public String getID() {
            return this.id;
        }

        @Override
        public void setID(String id) {
            this.id = id;
        }
    }

}
