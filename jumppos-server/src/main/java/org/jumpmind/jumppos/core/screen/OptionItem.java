package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String displayValue;
    protected String value;
    protected boolean enabled = true;
    protected boolean selected = false;
    
    public OptionItem() {
    }

    public OptionItem( String value ) {
        this( value, value );
    }

    public OptionItem( String value, String displayValue ) {
        this( value, displayValue, true );
    }

    public OptionItem( String value, String displayValue, boolean enabled ) {
        this.value = value;
        this.displayValue = displayValue;
        this.enabled = enabled;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    
}
