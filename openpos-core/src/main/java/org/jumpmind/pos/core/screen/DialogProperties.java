package org.jumpmind.pos.core.screen;

import java.io.Serializable;
/**
 * Refer to MatDialogConfig properties defined <a href="https://material.angular.io/components/dialog/api#MatDialogConfig">here</a>  
 * for list of properties that can be supported as needed.  Also includes OpenPOS specific properties
 */
public class DialogProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Use this to force a new dialog to be create if dialogs are being shown back to back.  The default behavior
     * is to reuse the previous dialog and just swap out the content.
     */
    private boolean forceReopen = false;
    private String width;
    private String height;
    private String minWidth;
    private String minHeight;
    private boolean executeActionBeforeClose = false;
    private boolean closeable = false;
    private boolean autoFocus = false;

    public DialogProperties() {
    }
    
    public DialogProperties(boolean closeable) {
        this.closeable = closeable;
    }
    
    public void setForceReopen(boolean forceReopen) {
        this.forceReopen = forceReopen;
    }
    
    public boolean isForceReopen() {
        return forceReopen;
    }
    
    public DialogProperties forceReopen(boolean forceReopen) {
        this.setForceReopen(forceReopen);
        return this;
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

    public boolean isExecuteActionBeforeClose() {
        return executeActionBeforeClose;
    }

    public void setExecuteActionBeforeClose(boolean executeActionBeforeClose) {
        this.executeActionBeforeClose = executeActionBeforeClose;
    }
    
    public DialogProperties executeActionBeforeClose(boolean execBeforeClose) {
        this.setExecuteActionBeforeClose(execBeforeClose);
        return this;
    }
    
    public boolean isCloseable() {
        return closeable;
    }

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

    public DialogProperties closeable(boolean closeable) {
        this.setCloseable(closeable);
        return this;
    }
	
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
    public DialogProperties height(String height) {
        this.setHeight(height);
        return this;
    }
	
	public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }
	
    public boolean isAutoFocus() {
        return autoFocus;
    }
    
    public DialogProperties autoFocus(boolean autoFocus) {
        this.setAutoFocus(autoFocus);
        return this;
    }

    public String getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }
    
}
