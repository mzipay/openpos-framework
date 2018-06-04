package org.jumpmind.pos.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.service.PosServerException;

public class DateUtils {
    
    private static final String ISO_DATE_TIME_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String ISO_DATE_TIME_SECONDS = "yyyy-MM-dd HH:mm:ss";
    private static final String ISO_DATE = "yyyy-MM-dd";
    
    private static String[] FORMATS = new String[] {
            ISO_DATE_TIME_MILLIS,
            ISO_DATE_TIME_SECONDS,
            ISO_DATE
    };
    
    public static Date parseDateTimeISO(String date) {
        if (!StringUtils.isEmpty(date)) {
            for (String format : FORMATS) {
                if (date.length() == format.length()) {                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                    try {
                        return dateFormat.parse(date);
                    } catch (ParseException ex) {
                        throw new PosServerException("Failed to parse date as ISO format: '" + date + "'", ex);
                    }
                }
            }
            throw new PosServerException("Failed to parse date as ISO format: '" + date + "'");
        } else {
            return null;            
        }
    }
    
    public static String formatDateTimeISO(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        } else {
            return "null";            
        }
    }

    public static long daysBetween(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance(); 
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance(); 
        cal2.setTime(date2);
        
        long diffDays = (cal2.getTimeInMillis()-cal1.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        return diffDays;
    }
    
}
