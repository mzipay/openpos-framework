package org.jumpmind.pos.core.ui.message;

import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.screen.ActionItem;
import org.jumpmind.pos.core.screen.IHasForm;
import org.jumpmind.pos.core.screen.IconType;
import org.jumpmind.pos.core.screen.KeyConstants;
import org.jumpmind.pos.core.screen.ScreenType;
import org.jumpmind.pos.core.ui.UIMessage;
import org.jumpmind.pos.core.ui.messagepart.SelfCheckoutMenuPart;

import java.util.ArrayList;
import java.util.List;

public class SelfCheckoutFormUIMessage extends UIMessage implements IHasForm {

    private static final long serialVersionUID = 1L;

    private SelfCheckoutMenuPart selfCheckoutMenu = new SelfCheckoutMenuPart();

    private Form form = new Form();

    private ActionItem submitButton;

    private List<String> alternateSubmitActions = new ArrayList<String>();

    public SelfCheckoutFormUIMessage() {
        setScreenType(ScreenType.SelfCheckoutForm);
        ActionItem submitButton = new ActionItem("Next", "Next", IconType.Forward);
        submitButton.setKeybind(KeyConstants.KEY_ENTER);
        setSubmitButton(submitButton);
    }

    public SelfCheckoutMenuPart getSelfCheckoutMenu() {
        return selfCheckoutMenu;
    }

    public void setSelfCheckoutMenu(SelfCheckoutMenuPart selfCheckoutMenu) {
        this.selfCheckoutMenu = selfCheckoutMenu;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ActionItem getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(ActionItem submitButton) {
        this.submitButton = submitButton;
    }

    public List<String> getAlternateSubmitActions() {
        return alternateSubmitActions;
    }

    public void setAlternateSubmitActions(List<String> alternateSubmitActions) {
        this.alternateSubmitActions = alternateSubmitActions;
    }

}
