package org.jumpmind.pos.core.model;

public enum FieldInputType {
    AlphanumericPassword,
    /** Allows alphabetic characters, numeric characters, and special characters */
    AlphanumericText,
    NumericText,
    Checkbox,
    ComboBox,
    Decimal,
    /** Intended to be used for reason code screens where a press of the reason code should cause the form to be submitted.*/
    SubmitOptionList,
    TextArea,
    StateIDNumber,
    Percent,
    PostalCode,
    Phone,
    Money,
    Income,
    Date,
    ToggleButton,
    AutoComplete,
    NoYearDate
}
