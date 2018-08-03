package org.jumpmind.pos.core.screen;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.Message;
import org.jumpmind.pos.core.template.AbstractTemplate;
import org.jumpmind.pos.core.template.BlankWithBarTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Screen extends Message {

    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private String icon;
    private AbstractTemplate template = new BlankWithBarTemplate();
    private String locale;
    private int sessionTimeoutMillis;
    private Action sessionTimeoutAction;

    public Screen() {
    }

    public Screen(String name, String type) {
        this.type = type;
        this.name = name;
    }

    /**
     * Allows this screen content to be displayed in a Dialog on the client
     * side.
     */
    public Screen asDialog() {
        return this.asDialog(null);
    }

    /**
     * Allows this screen content to be displayed in a Dialog on the client
     * side.
     * 
     * @param dialogProperties
     *            Additional properties that can control dialog behavior and
     *            rendering on the server side.
     */
    public Screen asDialog(DialogProperties dialogProperties) {
        this.template.setDialog(true);
        if (dialogProperties != null) {
            this.template.setDialogProperties(dialogProperties);
        }
        return this;
    }

    // TODO i don't really like this method here
    public static <T> T convertActionData(Object actionData, Class<T> convertToInstanceOf) {
        return new ObjectMapper().convertValue(actionData, convertToInstanceOf);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public boolean isScreenOfType(String type) {
        return this.type == type;
    }

    public void setBackButton(MenuItem backButton) {
        put("backButton", backButton);
    }

    public void setLogoutButton(MenuItem logoutButton) {
        put("logoutButton", logoutButton);
    }

    public void setTemplate(AbstractTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractTemplate> T getTemplate() {
        return (T)template;
    }

    public void setSequenceNumber(int sequenceNumber) {
        put("sequenceNumber", sequenceNumber);
    }

    public void setTheme(String theme) {
        this.put("theme", theme);
    }

    public void setReadOnly(boolean isReadOnly) {
        this.put("readOnly", isReadOnly);
    }

    public void setUseOnScreenKeyboard(boolean useOnScreenKeyboard) {
        this.put("useOnScreenKeyboard", useOnScreenKeyboard);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setRefreshAlways(boolean refreshAlways) {
        this.put("refreshAlways", refreshAlways);
    }

    public int getSessionTimeoutMillis() {
        return sessionTimeoutMillis;
    }

    public void setSessionTimeoutMillis(int sessionTimeoutMillis) {
        this.sessionTimeoutMillis = sessionTimeoutMillis;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setPrompt(String prompt) {
        this.put("prompt", prompt);
    }
    
    public void setSubtitle(String subtitle) {
        this.put("subtitle", subtitle);
    }
    
    /**
     * Indicator for marking the screen as "Customer Facing", meaning that the screen is intended for the customer to 
     * complete.
     * @param customerFacing <code>true</code> if the customer should use the screen.
     */
    public void setCustomerFacing(Boolean customerFacing) {
        put("customerFacing", customerFacing);
    }        
    
    public void setInstructions(String instructions) {
        this.put("instructions", instructions);
    }

    public Action getSessionTimeoutAction() {
        return sessionTimeoutAction;
    }

    public void setSessionTimeoutAction(Action sessionTimeoutAction) {
        this.sessionTimeoutAction = sessionTimeoutAction;
    }
}
