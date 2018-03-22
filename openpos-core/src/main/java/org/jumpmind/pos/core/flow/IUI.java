package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.flow.ui.PromptConfig;

public interface IUI {

    public void prompt(String message);
    public void prompt(PromptConfig promptConfig);
    
}
