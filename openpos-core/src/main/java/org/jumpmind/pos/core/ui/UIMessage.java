package org.jumpmind.pos.core.ui;

import org.jumpmind.pos.util.model.Message;

public class UIMessage extends Message {
    private String screenType;
    private String id;

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
