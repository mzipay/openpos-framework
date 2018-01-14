package org.jumpmind.pos.translate;

import java.util.Locale;

public interface ILegacyLocaleUtilities {
    public String formatComplexMessage(String pattern, Object[] vars);

    public Locale getLocale(String localeString);

}
