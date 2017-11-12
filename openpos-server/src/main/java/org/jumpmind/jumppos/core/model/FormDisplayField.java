package org.jumpmind.jumppos.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FormDisplayField implements IFormElement, IField, Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum ValueDisplayMode {
        Single,
        Multiple
    }
    
    private FieldElementType elementType = FieldElementType.Display;
    private String label;
    private String fieldId;
    private List<String> values = new ArrayList<String>();
    private ValueDisplayMode valueDisplayMode = ValueDisplayMode.Single;
    
    public FormDisplayField() {
    }

    public FormDisplayField(String label, String fieldId, String value) {
        this.label = label;
        this.fieldId = fieldId;
        this.setValue(value);
    }

    public FormDisplayField(String label, String fieldId, List<String> values) {
        this.label = label;
        this.fieldId = fieldId;
        this.values = new ArrayList<>(values);
    }
    
    public FieldElementType getElementType() {
        return elementType;
    }

    public void setElementType(FieldElementType elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getId() {
        return fieldId;
    }

    @Override
    public void setId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public String getValue() {
        return this.getValues().get(0);
    }
    
    public String getValue(int index) {
        return this.getValues().get(index);
    }
    
    @Override
    public void setValue(String value) {
        this.values.clear();
        this.values.add(value);
    }
    
    public void addValue(String value) {
        this.values.add(value);
    }
    
    public List<String> getValues() {
        return this.values;
    }
    
    public void setValues(List<String> values) {
        this.values = values;
    }

    public ValueDisplayMode getValueDisplayMode() {
        return valueDisplayMode;
    }

    public void setValueDisplayMode(ValueDisplayMode valueDisplayMode) {
        this.valueDisplayMode = valueDisplayMode;
    }

}
