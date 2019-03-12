package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class ActionItemGroup {
    
    private String title;
    private String keybind;
    private List<ActionItem> actionItems= new ArrayList<>();
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeybind() {
        return keybind;
    }
    public void setKeybind(String keybind) {
        this.keybind = keybind;
    }
    public List<ActionItem> getActionItems() {
        return actionItems;
    }
    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }
    
    

}
