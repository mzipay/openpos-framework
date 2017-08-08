package org.jumpmind.jumppos.pos.state;

import static org.jumpmind.jumppos.core.screen.DefaultScreen.FORM_SCREEN_TYPE;

import org.jumpmind.jumppos.core.model.annotations.FormButton;
import org.jumpmind.jumppos.core.model.annotations.FormTextField;
import org.jumpmind.jumppos.core.model.annotations.Screen;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

@Screen(name="NodePersonalization", type=FORM_SCREEN_TYPE)
public class NodePersonalizationForm extends DefaultScreen {
    
    @FormTextField(label="Node Id", placeholder="e.g. 00100-001", pattern="\\d{5}-\\d{3}")
    private String nodeId;
    
    @FormButton(label="Save")
    private final String SAVE_ACTION = "SavePersonalization"; 

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
}
