package org.jumpmind.pos.core.screen;


import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.DynamicList;
import org.jumpmind.pos.core.template.SellTemplate;

public class DynamicListScreen extends Screen {

    private static final long serialVersionUID = 1L;

    private DynamicList list = new DynamicList();
    
    private MenuItem submitButton;
    
    private List<String> alternateSubmitActions = new ArrayList<String>();

    public DynamicListScreen() {
        setScreenType(ScreenType.DynamicList);
        setTemplate(new SellTemplate());
        submitButton = new MenuItem("Next", "Next", "keyboard_arrow_right");
    }

    public void setList(DynamicList list) {
        this.list = list;
    }

    public DynamicList getList() {
        return this.list;
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

	public MenuItem getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(MenuItem submitButton) {
		this.submitButton = submitButton;
	}
}