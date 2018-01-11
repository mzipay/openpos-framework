package org.jumpmind.pos.core.model;

public class NumberMaskSpec implements IMaskSpec {

    public static final NumberMaskSpec DEFAULT_CURRENCY_SPEC = NumberMaskSpec.get().prefix("").includeThousandsSeparator(false).allowDecimal(true).integerLimit(9).requireDecimal(true).allowNegative(false);
    
    private static final long serialVersionUID = 1L;

    private MaskSpecType type = MaskSpecType.NumberMask;
    private String prefix = "";
    private String suffix = "";
    private boolean includeThousandsSeparator = true;
    private boolean allowDecimal = false;
    private String decimalSymbol = ".";
    private Integer decimalLimit = 2;
    private boolean requireDecimal = false;
    private boolean allowNegative = false;
    private boolean allowLeadingZeroes = false;
    private Integer integerLimit;
    
    public static NumberMaskSpec get() {
        return new NumberMaskSpec();
    }
    
    public NumberMaskSpec() {
        
    }
    
    public NumberMaskSpec(
       String prefix,
       String suffix,
       boolean includeThousandsSeparator,
       boolean allowDecimal,
       String decimalSymbol,
       Integer decimalLimit,
       boolean requireDecimal,
       boolean allowNegative,
       boolean allowLeadingZeroes,
       Integer integerLimit
            ) {
        this();
        this.prefix = prefix;
        this.suffix = suffix;
        this.includeThousandsSeparator = includeThousandsSeparator;
        this.allowDecimal = allowDecimal;
        this.decimalSymbol = decimalSymbol;
        this.decimalLimit = decimalLimit;
        this.requireDecimal = requireDecimal;
        this.allowNegative = allowNegative;
        this.allowLeadingZeroes = allowLeadingZeroes;
        this.integerLimit = integerLimit;
    }
    
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public NumberMaskSpec prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }
    
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public NumberMaskSpec suffix(String suffix) {
        this.setSuffix(suffix);
        return this;
    }
    
    public boolean isIncludeThousandsSeparator() {
        return includeThousandsSeparator;
    }
    public void setIncludeThousandsSeparator(boolean includeThousandsSeparator) {
        this.includeThousandsSeparator = includeThousandsSeparator;
    }
    public NumberMaskSpec includeThousandsSeparator(boolean includeThousandsSeparator) {
        this.setIncludeThousandsSeparator(includeThousandsSeparator);
        return this;
    }
    
    
    public boolean isAllowDecimal() {
        return allowDecimal;
    }
    public void setAllowDecimal(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }
    public NumberMaskSpec allowDecimal(boolean allowDecimal) {
        this.setAllowDecimal(allowDecimal);
        return this;
    }
    
    
    public String getDecimalSymbol() {
        return decimalSymbol;
    }
    public void setDecimalSymbol(String decimalSymbol) {
        this.decimalSymbol = decimalSymbol;
    }
    public NumberMaskSpec decimalSymbol(String decimalSymbol) {
        this.setDecimalSymbol(decimalSymbol);
        return this;
    }

    
    public Integer getDecimalLimit() {
        return decimalLimit;
    }
    public void setDecimalLimit(Integer decimalLimit) {
        this.decimalLimit = decimalLimit;
    }
    public NumberMaskSpec decimalLimit(Integer decimalLimit) {
        this.setDecimalLimit(decimalLimit);
        return this;
    }
    
    
    public boolean isRequireDecimal() {
        return requireDecimal;
    }
    public void setRequireDecimal(boolean requireDecimal) {
        this.requireDecimal = requireDecimal;
    }
    public NumberMaskSpec requireDecimal(boolean requireDecimal) {
        this.setRequireDecimal(requireDecimal);
        return this;
    }
    
    
    
    public boolean isAllowNegative() {
        return allowNegative;
    }
    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }
    public NumberMaskSpec allowNegative(boolean allowNegative) {
        this.setAllowNegative(allowNegative);
        return this;
    }
    
    
    public boolean isAllowLeadingZeroes() {
        return allowLeadingZeroes;
    }
    public void setAllowLeadingZeroes(boolean allowLeadingZeroes) {
        this.allowLeadingZeroes = allowLeadingZeroes;
    }
    public NumberMaskSpec allowLeadingZeroes(boolean allowLeadingZeroes) {
        this.setAllowLeadingZeroes(allowLeadingZeroes);
        return this;
    }
    
    
    public Integer getIntegerLimit() {
        return integerLimit;
    }
    public void setIntegerLimit(Integer integerLimit) {
        this.integerLimit = integerLimit;
    }
    public NumberMaskSpec integerLimit(Integer integerLimit) {
        this.setIntegerLimit(integerLimit);
        return this;
    }

    @Override
    public MaskSpecType getType() {
        return this.type;
    }
    
}
