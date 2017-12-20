package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.model.Form;

public class DynamicFormScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    private Form form = new Form();
    
    private String submitAction = "Next";

    public DynamicFormScreen() {
        setType(ScreenType.DynamicForm);
        setTemplate(DefaultScreen.TEMPLATE_SELL);
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public String getString(String id) {
        return form.getString(id);
    }
    
    public void setSubmitAction(String submitAction) {
        this.submitAction = submitAction;
    }
    
    public String getSubmitAction() {
        return submitAction;
    }
    
}
