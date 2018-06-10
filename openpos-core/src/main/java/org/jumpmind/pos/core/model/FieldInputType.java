package org.jumpmind.pos.core.model;

public enum FieldInputType {
    AlphanumericPassword,
    /** Allows alphabetic characters, numeric characters, and special characters */
    AlphanumericText,
    NumericText,
    Checkbox,
    ComboBox,
    PopTart,
    Decimal,
    TextArea,
    StateIDNumber,
    /** Allows positive decimal and integer values with at most two decimal places */
    Percent,
    /** Only allows values of 100, 0 or 2 digit positive integer percentage */
    PercentInt,
    PostalCode,
    Phone,
    Money,
    Income,
    Date,
    ToggleButton,
    AutoComplete,
    NoYearDate
}
