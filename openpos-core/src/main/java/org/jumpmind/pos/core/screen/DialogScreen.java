package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class DialogScreen extends SellScreen {

    private static final long serialVersionUID = 1L;
    
    private List<MenuItem> buttons = new ArrayList<>();
    
    private String title;
    
    private List<String> message = new ArrayList<>();
    
    private DialogProperties dialogProperties;

    public DialogScreen() {
        setType(ScreenType.Dialog);
    }
    
    public List<MenuItem> getButtons() {
        return buttons;
    }

    public void setButtons(List<MenuItem> buttons) {
        this.buttons = buttons;
    }
    
    public void addButton(MenuItem button) {
        this.buttons.add(button);
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

    protected DialogProperties getDialogProperties() {
        return dialogProperties;
    }

    protected void setDialogProperties(DialogProperties dialogProperties) {
        this.dialogProperties = dialogProperties;
    }
    
}
