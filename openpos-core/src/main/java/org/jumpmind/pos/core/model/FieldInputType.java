package org.jumpmind.pos.core.model;

public enum FieldInputType {
    AlphanumericPassword,
    AlphanumericText,
    NumericText,
    ComboBox,
    /** Intended to be used for reason code screens where a press of the reason code should cause the form to be submitted.*/
    SubmitOptionList,
    TextArea,
    Phone,
    Money
}
