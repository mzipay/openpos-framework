package org.jumpmind.pos.core.flow.ui;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.IUI;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;
import org.springframework.stereotype.Component;

@Component
public class UIManager implements IUI {
    
    private IStateManager stateManager;

    @Override
    public void prompt(String message) {
    }

    @Override
    public void prompt(PromptConfig promptConfig) {
        PromptScreen screen = new PromptScreen();
        if (promptConfig.getName() != null) {            
            screen.setName(promptConfig.getName());
        } else if (promptConfig.getPlaceholder() != null) {
            screen.setName(promptConfig.getPlaceholder());
        } else {
            screen.setName("Prompt " + StringUtils.abbreviate(promptConfig.getPromptText(), 25));
        }
        
        screen.setRefreshAlways(true);
        screen.setResponseType(promptConfig.getPromptType());
        if (promptConfig.getBackAction() != null) {            
            screen.setBackButton(new MenuItem(promptConfig.getBackAction(), "Back", true));
        }
        screen.setPromptIcon(promptConfig.getIcon());
        screen.setIcon(promptConfig.getIcon());
        screen.setPlaceholderText(promptConfig.getPlaceholder());
        screen.setText(promptConfig.getPromptText());
//        screen.setAction("Next");
        if (promptConfig.getActionMenuItem() != null) {            
            screen.setActionButton(promptConfig.getActionMenuItem());
        } else {
            screen.setActionButton(new MenuItem("Next", "UsernameEntered", true));
        }
        stateManager.showScreen(screen);
    }
    
    public void setStateManager(IStateManager stateManager) {
        this.stateManager = stateManager;
    }

}
