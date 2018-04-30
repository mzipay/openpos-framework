package org.jumpmind.pos.util;

import org.apache.commons.lang3.StringUtils;

public class AppUtils {
    
    public static boolean isDevMode() {
        String value = System.getProperty("profile");
        if (!StringUtils.isEmpty(value)) {
            return value.equalsIgnoreCase("dev");
        } else {
            return false;
        }
    }
}
