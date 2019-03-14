package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class SelectionListScreen extends Screen {

    private static final long serialVersionUID = 1L;

    private List<SelectionListItem> selectionList = new ArrayList<SelectionListItem>();

    private List<ActionItem> buttons = new ArrayList<>();
    
    private List<ActionItem> nonSelectionButtons = new ArrayList<>();

    private boolean multiSelect = false;
    
    private boolean defaultSelect = false;
    
    private String instructions;

    public SelectionListScreen() {
        this.setScreenType(ScreenType.SelectionList);
    }

    public List<SelectionListItem> getSelectionList() {
        return selectionList;
    }
    
    @Override
    public Screen asDialog() {
        this.setScreenType(ScreenType.SelectionListDialog);
        return (Screen)super.asDialog();
    }
    
    @Override
    public Screen asDialog(DialogProperties dialogProperties) {
        this.setScreenType(ScreenType.SelectionListDialog);
        return (Screen)super.asDialog(dialogProperties);
    }

    public void setSelectionList(List<SelectionListItem> selectionList) {
        this.selectionList = selectionList;
    }

    public void addSelection(SelectionListItem selection) {
        selectionList.add(selection);
    }

    public void addSelection(String title, SelectionListItemDisplayProperty column) {
        SelectionListItem selection = new SelectionListItem(title, column);
        selectionList.add(selection);
    }

    public List<ActionItem> getButtons() {
        return buttons;
    }

    public void setButtons(List<ActionItem> buttons) {
        this.buttons = buttons;
    }

    public void addButton(ActionItem button) {
        this.buttons.add(button);
    }

    public List<ActionItem> getNonSelectionButtons() {
        return nonSelectionButtons;
    }

    public void setNonSelectionButtons(List<ActionItem> nonSelectionButtons) {
        this.nonSelectionButtons = nonSelectionButtons;
    }

    public void addNonSelectionButton(ActionItem button) {
        this.nonSelectionButtons.add(button);
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
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}
