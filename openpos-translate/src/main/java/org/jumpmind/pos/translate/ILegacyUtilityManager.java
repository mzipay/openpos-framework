package org.jumpmind.pos.translate;

public interface ILegacyUtilityManager {
    String retrieveText(String specName, String bundleName, String propName, String defaultValue);
    String getPhoneValidationRegexp(String countryCode);
    String retrieveDialogText(String propName, String defaultValue);
}
