package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.model.Form;

// TODO: This could probably extend FormScreen
public class DynamicFormScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    private Form form = new Form();
    private MenuItem itemAction;

    public DynamicFormScreen() {
        setType(ScreenType.Form);
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public void setItemAction(MenuItem itemAction) {
        this.itemAction = itemAction;
    }
    
    public MenuItem getItemAction() {
        return itemAction;
    }
    
    public String getString(String id) {
        return form.getString(id);
    }
    
}
