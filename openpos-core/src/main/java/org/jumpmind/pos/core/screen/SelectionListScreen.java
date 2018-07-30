package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class SelectionListScreen extends Screen {

    private static final long serialVersionUID = 1L;

    private List<SelectionListItem> selectionList = new ArrayList<SelectionListItem>();

    private List<MenuItem> buttons = new ArrayList<>();

    private boolean multiSelect = false;

    public SelectionListScreen() {
        this.setType(ScreenType.SelectionList);
    }

    public List<SelectionListItem> getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(List<SelectionListItem> selectionList) {
        this.selectionList = selectionList;
    }

    public void addSelection(SelectionListItem selection) {
        selectionList.add(selection);
    }

    public void addSelection(String title, String body) {
        SelectionListItem selection = new SelectionListItem(title, body);
        selectionList.add(selection);
    }

    public List<MenuItem> getButtons() {
        return buttons;
    }

    public void setButtons(List<MenuItem> buttons) {
        this.buttons = buttons;
    }

    public void addButton(MenuItem button) {
        this.buttons.add(button);
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

}
