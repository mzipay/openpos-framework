package org.jumpmind.pos.translate;

import java.util.List;

public interface ILegacyCheckEntryBeanModel {

    List<String> getCheckIDTypes();
    int getSelectedCheckIDTypeIndex();
    void setSelectedCheckIDType(String idType);
    
    String getIDNumber();
    void setIDNumber(String idNumber);
    
    List<String> getCountryNames();
    int getSelectedCountryIndex();
    void setSelectedCountry(String country);
    
    List<String> getStateNames();
    int getSelectedStateIndex();
    void setSelectedState(String state);
    
    void setPhoneNumber(String phoneNumber);
    String getPhoneNumber();
    
    void setMICRNumber(String micrNumber);
    String getMICRNumber();
    
    void setCheckNumber(String checkNumber);
    String getCheckNumber();
}
