package org.jumpmind.jumppos.core.model;

import java.util.List;

import org.jumpmind.jumppos.core.screen.SelectionMode;

public class FormListField extends FormField {
    private static final long serialVersionUID = 1L;

    private List<String> values;
    private SelectionMode selectionMode = SelectionMode.Single;
    private int[] selectedIndexes = {};
    
    public FormListField() {
    }
    
    public FormListField(String fieldId, String placeholder) {
        super(fieldId, placeholder);
    }

    public FormListField(String fieldId, String label, String placeholder) {
        super(fieldId, label, placeholder);
    }
    
    public FormListField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, String placeholder) {
        super(fieldId, label, elementType, inputType, placeholder);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public int[] getSelectedIndexes() {
        return selectedIndexes;
    }

    public void setSelectedIndexes(int[] selectedIndexes) {
        this.selectedIndexes = selectedIndexes;
    }
    
    
    
}
