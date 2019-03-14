package org.jumpmind.pos.core.ui;

import org.jumpmind.pos.core.screen.ActionItem;

public interface IHasBackButton {
    ActionItem getBackButton();
    void setBackButton( ActionItem button);
}
