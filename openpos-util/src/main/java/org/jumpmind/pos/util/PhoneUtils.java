package org.jumpmind.pos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PhoneUtils {
    
    static final Logger logger = LoggerFactory.getLogger(PhoneUtils.class);
    
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            phoneNumber =  "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
        } 
        return phoneNumber;
    }
    
    public static String scrubPhone(String phoneNumber) {
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.replaceAll("\\(", "");
            phoneNumber = phoneNumber.replaceAll("\\)", "");
            phoneNumber = phoneNumber.replaceAll("-", "");
        }
        return phoneNumber;
    }
}
    