package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.model.FieldElementType;
import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.FormField;

public class LoginScreen extends DynamicFormScreen {

    private static final long serialVersionUID = 1L;

    private MenuItem changePasswordAction;
    private MenuItem forgotPasswordAction;
    
    private String subType;
    
    public LoginScreen() {
        setType(ScreenType.Login);
        setSubType(ScreenType.Login);
        
        FormField userIdField = new FormField("userId", null, FieldElementType.Input, FieldInputType.AlphanumericText, true, "");
        userIdField.setPlaceholder("User ID");
        getForm().addFormElement(userIdField);
        
        FormField passwordField = new FormField( "password", null, FieldElementType.Input, FieldInputType.AlphanumericPassword, true, "");
        passwordField.setPlaceholder("Password");
        getForm().addFormElement(passwordField);
        
    }
    
    public LoginScreen asDialog() {
    	setType(ScreenType.Dialog);
    	return this;
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

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

    
}
