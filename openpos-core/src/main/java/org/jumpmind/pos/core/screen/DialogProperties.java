package org.jumpmind.pos.core.screen;

import java.io.Serializable;
/**
 * Refer to MatDialogConfig properties defined <a href="https://material.angular.io/components/dialog/api#MatDialogConfig">here</a>  
 * for list of properties that can be supported as needed.
 */
public class DialogProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    private String width;

    public DialogProperties() {
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
    
}
