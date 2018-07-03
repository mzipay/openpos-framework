package org.jumpmind.pos.translate;

public interface ILegacyTenderLineItem {

    public ILegacyCurrency getAmountTender();
    public String getTypeDescriptorString();
    public String getTypeDescriptorDisplayString();
    public int getLineNumber();
    public String getNumber();
    public boolean isAuthorizableTender();
    public String getAuthorizationResponse();
}
