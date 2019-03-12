package org.jumpmind.pos.util;

import org.apache.commons.lang3.StringUtils;

public class BoxLogging {

    public static final String PLUS = "+";
    public static final String STAR = "*";
    public static final String UPPER_LEFT_CORNER = PLUS;
    public static final String UPPER_RIGHT_CORNER = PLUS;
    public static final String HORIZONTAL_MIDDLE = "-";
    public static final String HORIZONTAL_LINE = "-";
    public static final String VERITCAL_LINE = "|";
    public static final String LOWER_LEFT_CORNER = PLUS;
    public static final String LOWER_RIGHT_CORNER = PLUS;


    public static String box(String... text) {
        StringBuilder buff = new StringBuilder(System.getProperty("line.separator"));
        
        int longest = 0;
        for (String string : text) {
            if (longest < string.length()) {
                longest = string.length();
            }
        }
        
        int boxWidth = Math.max(longest + 6, 30);

        buff.append(PLUS).append(StringUtils.repeat(HORIZONTAL_MIDDLE, boxWidth - 2)).append(PLUS);
        buff.append("\r\n");
        for (String string : text) {
            buff.append(VERITCAL_LINE).append(StringUtils.center(string, boxWidth - 2)).append(VERITCAL_LINE);    
        }
        
        buff.append("\r\n");
        buff.append(PLUS).append(StringUtils.repeat(HORIZONTAL_MIDDLE, boxWidth - 2)).append(PLUS);
        return buff.toString();

    }
}
