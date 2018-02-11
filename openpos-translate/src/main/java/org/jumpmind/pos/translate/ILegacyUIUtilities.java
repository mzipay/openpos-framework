package org.jumpmind.pos.translate;

public interface ILegacyUIUtilities {
    String retrieveCommonText(String propName, String defaultValue);

    String retrieveCommonText(String propName);

    String retrieveText(String specName, String bundleName, String propName);
    String retrieveText(String specName, String bundleName, String propName, String defaultValue);
}
