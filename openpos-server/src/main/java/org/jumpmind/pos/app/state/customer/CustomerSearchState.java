package org.jumpmind.pos.app.state.customer;

import org.jumpmind.pos.app.state.AbstractState;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.screen.DynamicFormScreen;
import org.jumpmind.pos.core.screen.MenuItem;

public class CustomerSearchState extends AbstractState {
    
    @Override
    public void arrive(Action action) {
        DynamicFormScreen formScreen = new DynamicFormScreen();
        formScreen.setPrompt("Customer lookup");
        
        formScreen.setBackButton(new MenuItem("Back", "Back"));
        Form form = new Form();
        form.setName("Lookup Customer");
        form.addTextAreaField("firstName", "First Name", "", false);
        form.addTextAreaField("lastName", "First Name", "", false);
        form.addPostalCodeField("zip", "Zip", "", false);
        form.addPhoneField("phone", "Phone", "", false);
        formScreen.setForm(form);
        stateManager.showScreen(formScreen);
        
    }

    @Override
    protected String getDefaultBundleName() {
        return null;
    }

}
