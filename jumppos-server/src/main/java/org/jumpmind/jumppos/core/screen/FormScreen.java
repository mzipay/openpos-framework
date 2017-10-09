package org.jumpmind.jumppos.core.screen;

import org.jumpmind.jumppos.core.model.Form;

public class FormScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    Form form;

    public FormScreen() {
        setType(FORM_SCREEN_TYPE);
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

}
