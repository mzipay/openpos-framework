package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.model.Form;

public class FormScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    private Form form;
    private List<MenuItem> itemActions = new ArrayList<>();

    public FormScreen() {
        setType(ScreenType.Form);
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
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
