package org.jumpmind.pos.translate;

public interface ILegacyUtilityManager {
    public String retrieveText(String specName, String bundleName, String propName, String defaultValue);
    public String getPhoneValidationRegexp(String countryCode);
}
