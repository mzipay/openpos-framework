package org.jumpmind.pos.core.screen;


public class ReturnItemScreen extends SellItemScreen {
    private static final long serialVersionUID = 1L;

    private SelectionMode selectionMode = SelectionMode.Multiple;

    public ReturnItemScreen() {
        super();
        this.setScreenType(ScreenType.ReturnItem);
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

}
