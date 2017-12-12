package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.model.FieldElementType;
import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.FormButton;
import org.jumpmind.pos.core.model.FormField;

public class LoginScreen extends FormScreen {

    private static final long serialVersionUID = 1L;

    private MenuItem changePasswordAction;
    private MenuItem forgotPasswordAction;
    
    private MenuItem submitAction;
    
    public LoginScreen() {
        setType(ScreenType.Login);
        
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
    
    public MenuItem getChangePasswordAction() {
        return changePasswordAction;
    }

    public void setChangePasswordAction(MenuItem changePasswordAction) {
        this.changePasswordAction = changePasswordAction;
    }

    public MenuItem getForgotPasswordAction() {
        return forgotPasswordAction;
    }

    public void setForgotPasswordAction(MenuItem forgotPasswordAction) {
        this.forgotPasswordAction = forgotPasswordAction;
    }

    /**
     * An alternate action that can be used if the OK button is not desired.
     * @return The action to be used in place of the 'OK' button.
     */
    public MenuItem getSubmitAction() {
        return submitAction;
    }

    /**
     * Sets a new action that can be used in place of the OK button.
     * @param submitAction The action to replace the function of the OK button.
     */
    public void setSubmitAction(MenuItem submitAction) {
        this.submitAction = submitAction;
    }
    
}
