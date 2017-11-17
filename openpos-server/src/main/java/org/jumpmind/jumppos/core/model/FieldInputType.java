package org.jumpmind.jumppos.core.model;

public enum FieldInputType {
    AlphanumericText,
    NumericText,
    ComboBox,
    /** Intended to be used for reason code screens where a press of the reason code should cause the form to be submitted.*/
    SubmitOptionList
}
