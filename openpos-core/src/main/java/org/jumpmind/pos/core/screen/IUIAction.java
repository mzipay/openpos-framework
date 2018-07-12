package org.jumpmind.pos.core.screen;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public interface IUIAction {
    public String getAction();
    public void setAction(String action);

    public String getTitle();
    public void setTitle(String title);

    public String getIcon();
    public void setIcon(String icon);

    public void setEnabled(boolean enabled);
    public boolean isEnabled();

}
