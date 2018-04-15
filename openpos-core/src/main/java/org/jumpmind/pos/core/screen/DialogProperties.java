package org.jumpmind.pos.core.screen;

import java.io.Serializable;
/**
 * Refer to MatDialogConfig properties defined <a href="https://material.angular.io/components/dialog/api#MatDialogConfig">here</a>  
 * for list of properties that can be supported as needed.  Also includes OpenPOS specific properties
 */
public class DialogProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    private String width;
    private String height;
    private Boolean executeActionBeforeClose;
    private Boolean closeable;

    public DialogProperties() {
    }
    
    public DialogProperties(boolean closeable) {
        this.closeable = closeable;
    }
    
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
    
    public DialogProperties width(String width) {
        this.setWidth(width);
        return this;
    }

    public Boolean getExecuteActionBeforeClose() {
        return executeActionBeforeClose;
    }

    public void setExecuteActionBeforeClose(Boolean executeActionBeforeClose) {
        this.executeActionBeforeClose = executeActionBeforeClose;
    }
    
    public DialogProperties executeActionBeforeClose(boolean execBeforeClose) {
        this.setExecuteActionBeforeClose(execBeforeClose);
        return this;
    }

	public Boolean getCloseable() {
		return closeable;
	}

	public void setCloseable(Boolean closeable) {
		this.closeable = closeable;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
