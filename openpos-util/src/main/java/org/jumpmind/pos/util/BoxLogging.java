package org.jumpmind.pos.util;

import org.apache.commons.lang3.StringUtils;

public class BoxLogging {

    public static final String STAR = "*";
    public static final String UPPER_LEFT_CORNER = "|";
    public static final String UPPER_RIGHT_CORNER = "|";
    public static final String HORIZONTAL_MIDDLE = "-";
    public static final String HORIZONTAL_LINE = "_";
    public static final String VERITCAL_LINE = "|";
    public static final String LOWER_LEFT_CORNER = "|";
    public static final String LOWER_RIGHT_CORNER = "|";
    public static final String PLUS = "+";

    public static String box(String text) {
        StringBuilder buff = new StringBuilder("\n");
        int boxWidth = Math.max(text.length() + 6, 30);

        buff.append(PLUS).append(StringUtils.repeat(HORIZONTAL_MIDDLE, boxWidth - 2)).append(PLUS);
        buff.append("\r\n");
        buff.append(VERITCAL_LINE).append(StringUtils.center(text, boxWidth - 2)).append(VERITCAL_LINE);
        buff.append("\r\n");
        buff.append(PLUS).append(StringUtils.repeat(HORIZONTAL_MIDDLE, boxWidth - 2)).append(PLUS);
        return buff.toString();

    }
}
