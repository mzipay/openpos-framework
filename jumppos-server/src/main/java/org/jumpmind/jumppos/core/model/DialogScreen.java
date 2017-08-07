package org.jumpmind.jumppos.core.model;

import java.util.ArrayList;
import java.util.List;

public class DialogScreen extends DefaultScreen {

    String subType;
    
    List<MenuItem> buttons = new ArrayList<>();
    
    String title;
    
    List<String> message = new ArrayList<>();

    public DialogScreen() {
        setType(DIALOG_SCREEN_TYPE);
    }
    
    public void setSubType(String subType) {
        this.subType = subType;
    }
    
    public String getSubType() {
        return subType;
    }

    public List<MenuItem> getButtons() {
        return buttons;
    }

    public void setButtons(List<MenuItem> buttons) {
        this.buttons = buttons;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
    
    public List<String> getMessage() {
        return message;
    }
    
}
