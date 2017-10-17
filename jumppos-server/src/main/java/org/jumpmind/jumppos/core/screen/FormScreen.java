package org.jumpmind.jumppos.core.screen;

import org.jumpmind.jumppos.core.model.Form;

public class FormScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    private Form form;
    
    public FormScreen() {
        setType(ScreenType.Form);
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

}
