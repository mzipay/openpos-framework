package org.jumpmind.jumppos.pos.state;

import org.jumpmind.jumppos.core.model.annotations.FormButton;
import org.jumpmind.jumppos.core.model.annotations.FormTextField;
import org.jumpmind.jumppos.core.model.annotations.Screen;
import static org.jumpmind.jumppos.core.model.IScreen.FORM_SCREEN_TYPE;

@Screen(name="NodePersonalization", type=FORM_SCREEN_TYPE)
public class NodePersonalizationForm extends org.jumpmind.jumppos.core.model.Screen {
    
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
