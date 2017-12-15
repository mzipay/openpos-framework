package org.jumpmind.pos.core.model;

import java.util.List;

public class ComboField extends FormField {
    private static final long serialVersionUID = 1L;

    private List<String> values;
    
    public ComboField() {
        setInputType(FieldInputType.ComboBox);
        setElementType(FieldElementType.Input);
    }
    
    public ComboField(String fieldId, String placeholder) {
        super(fieldId, placeholder);
        setInputType(FieldInputType.ComboBox);
        setElementType(FieldElementType.Input);
    }

    public ComboField(String fieldId, String label, String placeholder) {
        super(fieldId, label, placeholder);
        setInputType(FieldInputType.ComboBox);
        setElementType(FieldElementType.Input);

    }

    public ComboField(String fieldId, String label, String placeholder, List<String> values) {
        super(fieldId, label, placeholder);
        setInputType(FieldInputType.ComboBox);
        setElementType(FieldElementType.Input);
        this.values = values;
    }
    
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
    
}
