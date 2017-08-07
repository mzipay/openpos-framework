package org.jumpmind.jumppos.pos.state;

import static org.jumpmind.jumppos.core.screen.DefaultScreen.FORM_SCREEN_TYPE;

import org.jumpmind.jumppos.core.model.annotations.FormButton;
import org.jumpmind.jumppos.core.model.annotations.FormTextField;
import org.jumpmind.jumppos.core.model.annotations.Screen;

@Screen(name="NodePersonalization", type=FORM_SCREEN_TYPE)
public class NodePersonalizationForm extends org.jumpmind.jumppos.core.screen.DefaultScreen {
    
    @FormTextField(label="Node Id:", placeholder="e.g. 100-1")
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
