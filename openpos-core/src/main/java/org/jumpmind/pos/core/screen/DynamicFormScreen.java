package org.jumpmind.pos.core.screen;


import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.template.SellTemplate;

public class DynamicFormScreen extends Screen implements IHasForm {

    private static final long serialVersionUID = 1L;

    private Form form = new Form();
    
    private MenuItem submitButton;
    
    private List<String> alternateSubmitActions = new ArrayList<String>();

    public DynamicFormScreen() {
        setScreenType(ScreenType.DynamicForm);
        setTemplate(new SellTemplate());
        submitButton = new MenuItem("Next", "Next", "keyboard_arrow_right");
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
    
    @Deprecated
    public void setSubmitAction(String submitAction) {
        submitButton.setAction(submitAction);
    }
    @Deprecated
    public String getSubmitAction() {
        return submitButton.getAction();
    }
    
    public void addAlternateSubmitAction(String action) {
        this.alternateSubmitActions.add( action );
    }

    public List<String> getAlternateSubmitActions() {
        return alternateSubmitActions;
    }

    public void setAlternateSubmitActions(List<String> alternateActions) {
        this.alternateSubmitActions = alternateActions;
    }

    @Deprecated
	public String getSubmitButtonText() {
		return submitButton.getTitle();
	}

    @Deprecated
	public void setSubmitButtonText(String submitButtonText) {
		submitButton.setTitle(submitButtonText);
	}

    @Deprecated
	public String getSubmitIcon() {
		return submitButton.getIcon();
	}

    @Deprecated
	public void setSubmitIcon(String submitIcon) {
		this.submitButton.setIcon(submitIcon);
	}

	public MenuItem getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(MenuItem submitButton) {
		this.submitButton = submitButton;
	}
    
}
