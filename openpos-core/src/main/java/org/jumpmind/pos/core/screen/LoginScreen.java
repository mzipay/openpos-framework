package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.model.FieldElementType;
import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.FormButton;
import org.jumpmind.pos.core.model.FormField;

public class LoginScreen extends FormScreen {

    private static final long serialVersionUID = 1L;

    public LoginScreen() {
        setScreenSubtype("login");
        
        FormField userIdField = new FormField("userId", null, FieldElementType.Input, FieldInputType.AlphanumericText, true, "");
        userIdField.setPlaceholder("User ID");
        getForm().addFormElement(userIdField);
        
        FormField passwordField = new FormField( "password", null, FieldElementType.Input, FieldInputType.AlphanumericPassword, true, "");
        passwordField.setPlaceholder("Password");
        getForm().addFormElement(passwordField);
        
        FormButton okButton = new FormButton("okButton", "OK", "OK");
        getForm().addFormElement(okButton);
        FormButton cancelButton = new FormButton("cancelButton", "Cancel", "Cancel");
        getForm().addFormElement(cancelButton);
    }
}
