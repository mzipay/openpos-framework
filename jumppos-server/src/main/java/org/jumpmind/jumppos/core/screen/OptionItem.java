package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String displayValue;
    private String value;
    private boolean enabled = true;
    private boolean selected = false;
    private String icon;

    public OptionItem() {
    }

    public OptionItem(String value) {
        this(value, value);
    }

    public OptionItem(String value, String displayValue) {
        this(value, displayValue, true);
    }

    public OptionItem(String value, String displayValue, boolean enabled) {
        this(value, displayValue, enabled, (String) null);
    }

    public OptionItem(String value, String displayValue, boolean enabled, String icon) {
        this.value = value;
        this.displayValue = displayValue;
        this.enabled = enabled;
        this.icon = icon;
    }

    public OptionItem(String value, String displayValue, boolean enabled, IIcon icon) {
        this(value, displayValue, enabled, icon.getName());
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
