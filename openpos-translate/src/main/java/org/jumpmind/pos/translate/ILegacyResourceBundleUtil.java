package org.jumpmind.pos.translate;

import java.util.Locale;
import java.util.Properties;

public interface ILegacyResourceBundleUtil {
    Properties getText(String bundleName, Locale locale);
    Properties getGroupText(String groupName, String[] bundleNames, Locale locale);
}
