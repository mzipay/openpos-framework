package org.jumpmind.pos.translate;

import java.util.Locale;
import java.util.Properties;

public interface ILegacyResourceBundleUtil {
    public Properties getText(String bundleName, Locale locale);
}
