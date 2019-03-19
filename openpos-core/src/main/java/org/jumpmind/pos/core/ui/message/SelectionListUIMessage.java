package org.jumpmind.pos.core.ui.message;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.screen.ActionItem;
import org.jumpmind.pos.core.screen.SelectionListItem;
import org.jumpmind.pos.core.ui.UIMessage;
import org.jumpmind.pos.core.ui.messagepart.BaconStripPart;

public class SelectionListUIMessage extends UIMessage {

    private static final long serialVersionUID = -4859870631964238380L;

    private List<SelectionListItem> selectionList = new ArrayList<SelectionListItem>();
    private List<ActionItem> buttons = new ArrayList<>();
    private List<ActionItem> nonSelectionButtons = new ArrayList<>();
    private boolean multiSelect = false;
    private boolean defaultSelect = false;
    private int defaultSelectItemIndex = 0;
    private String selectionChangedAction;
    private String instructions;
    
    public SelectionListUIMessage() {
        this.setScreenType(UIMessageType.SELECTION_LIST);
    }

    public List<SelectionListItem> getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(List<SelectionListItem> selectionList) {
        this.selectionList = selectionList;
    }

    public List<ActionItem> getButtons() {
        return buttons;
    }

    public void setButtons(List<ActionItem> buttons) {
        this.buttons = buttons;
    }
    
    public void addButton(ActionItem button) {
        if(this.buttons == null) {
            this.buttons = new ArrayList<ActionItem>();
        }
        this.buttons.add(button);
    }

    public List<ActionItem> getNonSelectionButtons() {
        return nonSelectionButtons;
    }

    public void setNonSelectionButtons(List<ActionItem> nonSelectionButtons) {
        this.nonSelectionButtons = nonSelectionButtons;
    }
    
    public void addNonSelectionButton(ActionItem nonSelectionButton) {
        if(this.nonSelectionButtons == null) {
            this.nonSelectionButtons = new ArrayList<ActionItem>();
        }
        this.nonSelectionButtons.add(nonSelectionButton);
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public boolean isDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(boolean defaultSelect) {
        this.defaultSelect = defaultSelect;
    }

    public int getDefaultSelectItemIndex() {
        return defaultSelectItemIndex;
    }

    public void setDefaultSelectItemIndex(int defaultSelectItemIndex) {
        this.defaultSelectItemIndex = defaultSelectItemIndex;
    }

    public String getSelectionChangedAction() {
        return selectionChangedAction;
    }

    public void setSelectionChangedAction(String selectionChangedAction) {
        this.selectionChangedAction = selectionChangedAction;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}